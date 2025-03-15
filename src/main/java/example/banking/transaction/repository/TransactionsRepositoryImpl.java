package example.banking.transaction.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.transaction.dto.TransactionDto;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.rowMapper.TransactionRowMapper;
import example.banking.transaction.types.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionsRepositoryImpl
    extends AbstractRepository<Transaction, TransactionDto>
    implements TransactionsRepository {

    @Autowired
    public TransactionsRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = new TransactionRowMapper();
    }

    @Override
    public List<Transaction> findAllByUserId(Long userId) {
        String sql = """
            WITH client_accounts AS (
                SELECT id FROM account WHERE holder_id = (SELECT id FROM client WHERE user_id = :user_id)
            ),
            client_loans AS (
                SELECT id FROM loan WHERE account_id IN (SELECT id FROM client_accounts)
            ),
            client_deposits AS (
                SELECT id FROM deposit WHERE account_id IN (SELECT id FROM client_accounts)
            )
            SELECT *
            FROM transaction
            WHERE from_entity_id IN (
                SELECT id FROM client_account
                UNION
                SELECT id FROM client_loans
                UNION
                SELECT id FROM client_deposits
            )
            OR to_entity_id IN (
                SELECT id FROM client_accounts
                UNION
                SELECT id FROM client_loans
                UNION
                SELECT id FROM client_deposits
            )
            ORDER BY timestamp;
        """;

        var map = new MapSqlParameterSource("user_id", userId);

        return findAllByCriteria(sql, map);
    }

    @Override
    public List<Transaction> findAllByEntityId(Long entityId, TransactionType type) {
        String sql = """
            SELECT *
            FROM public.transaction
            WHERE
                (from_entity_id = :entity_id OR to_entity_id = :entity_id)
                AND
                (from_type = :type OR to_type = :type)
        """;

        var map = new MapSqlParameterSource();
        map
                .addValue("entity_id", entityId)
                .addValue("type", type.toString());

        return findAllByCriteria(sql, map);
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM public.transaction";
    }

    @Override
    protected String getFindByIdSql() {
        return "SELECT * FROM public.transaction WHERE id = :id";
    }

    @Override
    protected String getCreateSql() {
        return """
            INSERT INTO public.transaction
                (from_entity_id, from_type, to_entity_id, to_type, amount, timestamp, revert_transaction_id)
            VALUES
                (:from_entity_id, :from_type, :to_entity_id, :to_type, :amount, :timestamp, :revert_transaction_id)
            RETURNING id;
        """;
    }

    @Override
    protected String getUpdateSql() {
        return """
            UPDATE public.transaction
            SET
                from_entity_id = :from_entity_id,
                from_type = :from_type,
                to_entity_id = :to_entity_id,
                to_type = :to_type,
                amount = :amount,
                timestamp = :timestamp,
                revert_transaction_id = :revert_transaction_id
            WHERE
                id = :id
        """;
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM public.transaction WHERE id = :id";
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Transaction transaction) {
        var dto = transaction.toDto();
        var map = new MapSqlParameterSource();

        map
                .addValue("from_entity_id", dto.getFromEntityId())
                .addValue("from_type", dto.getFromType().toString())
                .addValue("to_entity_id", dto.getToEntityId())
                .addValue("revert_transaction_id", dto.getRevertTransactionId())
                .addValue("to_type", dto.getToType().toString())
                .addValue("amount", dto.getAmount())
                .addValue("timestamp", dto.getTimestamp());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Transaction transaction) {
        var map = getMapSqlParameterSource(transaction);
        map.addValue("id", transaction.getId());

        return map;
    }

    @Override
    protected Transaction fromDto(TransactionDto dto) {
        return Transaction.fromDto(dto);
    }
}
