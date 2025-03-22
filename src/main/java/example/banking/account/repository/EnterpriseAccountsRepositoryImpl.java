package example.banking.account.repository;

import example.banking.account.dto.EnterpriseAccountDto;
import example.banking.account.entity.EnterpriseAccount;
import example.banking.account.rowMapper.EnterpriseAccountRowMapper;
import example.banking.contracts.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EnterpriseAccountsRepositoryImpl
        extends AbstractRepository<EnterpriseAccount, EnterpriseAccountDto>
        implements EnterpriseAccountsRepository {

    @Autowired
    public EnterpriseAccountsRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    protected RowMapper<EnterpriseAccountDto> getRowMapper() {
        return new EnterpriseAccountRowMapper();
    }

    @Override
    public Optional<EnterpriseAccount> findBySalaryProjectId(Long salaryProjectId) {
        String sql = """
            SELECT a.*, ea.specialist_id, ea.enterprise_id FROM enterprise_account ea
            LEFT JOIN account a ON a.id = ea.id
            LEFT JOIN enterprise e ON ea.enterprise_id = e.id
            LEFT JOIN salary_project sp ON sp.account_id = a.id
            WHERE  sp.id = :salary_project_id
        """;

        var map = new MapSqlParameterSource("salary_project_id", salaryProjectId);

        return findByCriteria(sql, map);
    }

    @Override
    public List<EnterpriseAccount> findAllByEnterpriseId(Long enterpriseId) {
        String sql = """
                SELECT a.*, ea.specialist_id, ea.enterprise_id FROM enterprise_account ea
                LEFT JOIN account a ON a.id = ea.id
                LEFT JOIN enterprise e ON ea.enterprise_id = e.id
                WHERE e.id = :enterprise_id
        """;

        var map = new MapSqlParameterSource("enterprise_id", enterpriseId);

        return findAllByCriteria(sql, map);
    }

    @Override
    public List<EnterpriseAccount> findAllBySpecialistId(Long specialistId) {
        String sql = """
                SELECT a.*, ea.specialist_id, ea.enterprise_id FROM enterprise_account ea
                LEFT JOIN account a ON a.id = ea.id
                LEFT JOIN specialist s ON s.id = ea.specialist_id
                WHERE s.id = :specialist_id
        """;

        var map = new MapSqlParameterSource("specialist_id", specialistId);

        return findAllByCriteria(sql, map);
    }

    @Override
    protected String getFindAllSql() {
        return """
                SELECT a.*, ea.specialist_id, ea.enterprise_id FROM enterprise_account ea
                LEFT JOIN account a ON a.id = ea.id
        """;
    }

    @Override
    protected String getFindByIdSql() {
        return """
                SELECT a.*, ea.specialist_id, ea.enterprise_id FROM enterprise_account ea
                LEFT JOIN account a ON a.id = ea.id
                WHERE ea.id = :id
        """;
    }

    @Override
    protected String getCreateSql() {
        return """
                WITH inserted_account AS (
                    INSERT INTO account (iban, status, balance, created_at, bank_id)
                    VALUES (:iban, :status, :balance, :created_at, :bank_id)
                    RETURNING id
                )
                INSERT INTO enterprise_account(id, enterprise_id, specialist_id)
                VALUES ( (SELECT id FROM inserted_account), :enterprise_id, :specialist_id )
                RETURNING id
        """;
    }

    @Override
    protected String getUpdateSql() {
        return """
                WITH updated_account AS (
                    UPDATE account SET
                        iban = :iban,
                        status = :status,
                        balance = :balance,
                        created_at = :created_at
                    WHERE id = :id
                    RETURNING id
                )
                UPDATE enterprise_account SET
                    specialist_id = :specialist_id
                WHERE id = :id
        """;
    }

    @Override
    protected String getDeleteSql() {
        return "";
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(EnterpriseAccount account) {
        var map = new MapSqlParameterSource();
        var dto = account.toDto();

        map
                .addValue("iban", dto.getIBAN())
                .addValue("status", dto.getStatus().toString())
                .addValue("balance", dto.getBalance())
                .addValue("created_at", dto.getCreatedAt())
                .addValue("bank_id", dto.getBankId())
                .addValue("enterprise_id", dto.getEnterpriseId())
                .addValue("specialist_id", dto.getSpecialistId());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(EnterpriseAccount account) {
        var map = getMapSqlParameterSource(account);

        map.addValue("id", account.getId());

        return map;
    }

    @Override
    protected EnterpriseAccount fromDto(EnterpriseAccountDto dto) {
        return EnterpriseAccount.fromDto(dto);
    }
}
