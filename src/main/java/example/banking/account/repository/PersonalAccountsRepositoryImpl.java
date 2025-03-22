package example.banking.account.repository;

import example.banking.account.dto.PersonalAccountDto;
import example.banking.account.entity.PersonalAccount;
import example.banking.account.rowMapper.PersonalAccountRowMapper;
import example.banking.contracts.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonalAccountsRepositoryImpl
        extends AbstractRepository<PersonalAccount, PersonalAccountDto>
        implements PersonalAccountsRepository {

    @Autowired
    public PersonalAccountsRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    public List<PersonalAccount> findAllByHolderId(Long holderId) {
        String sql = """
                SELECT a.*, pa.holder_id FROM personal_account pa
                LEFT JOIN account a ON a.id = pa.id
                WHERE pa.holder_id = :holder_id;
        """;

        var map = new MapSqlParameterSource("holder_id", holderId);

        return findAllByCriteria(sql, map);
    }

    @Override
    protected String getCreateSql() {
        return """
                WITH inserted_account AS (
                    INSERT INTO account (iban, status, balance, created_at, bank_id)
                    VALUES (:iban, :status, :balance, :created_at, :bank_id)
                    RETURNING id
                )
                INSERT INTO personal_account(id, holder_id)
                VALUES ( (SELECT id FROM inserted_account), :holder_id )
                RETURNING id
        """;
    }

    @Override
    protected String getUpdateSql() {
        return """
                UPDATE account SET
                    iban = :iban,
                    status = :status,
                    balance = :balance,
                    created_at = :created_at
                WHERE id = :id;

                UPDATE personal_account SET
                    holder_id = :holder_id
                WHERE id = :id
        """;
    }

    @Override
    protected String getFindAllSql() {
        return """
                SELECT a.*, pa.holder_id FROM personal_account pa
                LEFT JOIN account a ON a.id = pa.id
        """;
    }

    @Override
    protected String getFindByIdSql() {
        return """
                SELECT a.*, pa.holder_id FROM personal_account pa
                LEFT JOIN account a ON a.id = pa.id
                WHERE pa.id = :id
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
    protected PersonalAccount fromDto(PersonalAccountDto dto) {
        return PersonalAccount.fromDto(dto);
    }


    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(PersonalAccount account) {
        var accountDto = account.toDto();
        var map = new MapSqlParameterSource();
        map.addValue("iban", accountDto.getIBAN());
        map.addValue("status", accountDto.getStatus().toString());
        map.addValue("balance", accountDto.getBalance());
        map.addValue("created_at", accountDto.getCreatedAt());
        map.addValue("holder_id", accountDto.getHolderId());
        map.addValue("bank_id", accountDto.getBankId());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(PersonalAccount account) {
        var map = getMapSqlParameterSource(account);
        map.addValue("id", account.getId());
        return map;
    }

    @Override
    protected RowMapper<PersonalAccountDto> getRowMapper() {
        return new PersonalAccountRowMapper();
    }
}
