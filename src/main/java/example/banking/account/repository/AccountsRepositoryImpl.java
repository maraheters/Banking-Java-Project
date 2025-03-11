package example.banking.account.repository;

import example.banking.account.dto.AccountDto;
import example.banking.account.entity.Account;
import example.banking.account.rowMapper.AccountRowMapper;
import example.banking.contracts.AbstractRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountsRepositoryImpl extends AbstractRepository<Account, AccountDto> implements AccountsRepository {

    public AccountsRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = new AccountRowMapper();
    }

    @Override
    public List<Account> findByHolderId(Long holderId) {
        String sql = "SELECT * FROM account WHERE holder_id = :holder_id";
        var map = new MapSqlParameterSource("holder_id", holderId);

        return findAllByCriteria(sql, map);
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        String sql = """
            SELECT a.*
            FROM account a
            LEFT JOIN client c ON a.holder_id = c.id
            WHERE c.user_id = :user_id
        """;

        var map = new MapSqlParameterSource("user_id", userId);

        return findAllByCriteria(sql, map);
    }

    @Override
    protected String getCreateSql() {
        return """
            INSERT INTO account (iban, status, type, balance, date_created, holder_id, bank_id)
            VALUES (:iban, :status, :type, :balance, :date_created, :holder_id, :bank_id)
            RETURNING id
        """;
    }

    @Override
    protected String getUpdateSql() {
        return """
            UPDATE account SET
            iban = :iban,
            status = :status,
            type = :type,
            balance = :balance,
            date_created = :date_created,
            holder_id = :holder_id
            WHERE id = :id
        """;
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM account a";
    }

    @Override
    protected String getFindByIdSql() {
        return """
                SELECT *
                FROM account
                WHERE account.id = :id
        """;
    }

    @Override
    protected String getDeleteSql() {
        return """
            DELETE *
            FROM account
            WHERE account.id = :id
        """;
    }

    @Override
    protected Account fromDto(AccountDto dto) {
        return Account.fromDto(dto);
    }


    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Account account) {
        var accountDto = account.toDto();
        var map = new MapSqlParameterSource();
        map.addValue("iban", accountDto.getIBAN());
        map.addValue("status", accountDto.getStatus().toString());
        map.addValue("type", accountDto.getType().toString());
        map.addValue("balance", accountDto.getBalance());
        map.addValue("date_created", accountDto.getDateCreated());
        map.addValue("holder_id", accountDto.getHolderId());
        map.addValue("bank_id", accountDto.getBankId());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Account account) {
        var map = getMapSqlParameterSource(account);
        map.addValue("id", account.getId());
        return map;
    }
}
