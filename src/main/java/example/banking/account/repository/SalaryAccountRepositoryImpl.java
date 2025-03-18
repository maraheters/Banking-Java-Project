package example.banking.account.repository;

import example.banking.account.dto.SalaryAccountDto;
import example.banking.account.entity.SalaryAccount;
import example.banking.account.rowMapper.SalaryAccountRowMapper;
import example.banking.contracts.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalaryAccountRepositoryImpl
    extends AbstractRepository<SalaryAccount, SalaryAccountDto>
    implements SalaryAccountsRepository {

    @Autowired
    public SalaryAccountRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    protected RowMapper<SalaryAccountDto> getRowMapper() {
        return new SalaryAccountRowMapper();
    }

    @Override
    public List<SalaryAccount> findAllBySalaryProjectId(Long id) {
        var sql = """
                SELECT a.*, sa.holder_id, sa.salary_project_id, sa.salary
                FROM salary_account sa
                LEFT JOIN account a ON a.id = sa.id
                WHERE sa.salary_project_id = :salary_project_id
        """;

        var map = new MapSqlParameterSource("salary_project_id", id);

        return findAllByCriteria(sql, map);
    }

    @Override
    protected String getFindAllSql() {
        return """
                SELECT a.*, sa.holder_id, sa.salary_project_id, sa.salary
                FROM salary_account sa
                LEFT JOIN account a ON a.id = sa.id
        """;
    }

    @Override
    protected String getFindByIdSql() {
        return """
                SELECT a.*, sa.holder_id, sa.salary_project_id, sa.salary
                FROM salary_account sa
                LEFT JOIN account a ON a.id = sa.id
                WHERE sa.id = :id
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
                INSERT INTO salary_account(id, holder_id, salary_project_id, salary)
                VALUES ( (SELECT id FROM inserted_account), :holder_id, :salary_project_id, :salary)
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

                UPDATE salary_account SET
                    holder_id = :holder_id,
                    salary_project_id = :salary_project_id,
                    salary = :salary
                WHERE id = :id
        """;
    }

    @Override
    protected String getDeleteSql() {
        return "";
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(SalaryAccount account) {
        var map = new MapSqlParameterSource();
        var dto = account.toDto();

        map
                .addValue("iban", dto.getIBAN())
                .addValue("status", dto.getStatus().toString())
                .addValue("balance", dto.getBalance())
                .addValue("created_at", dto.getCreatedAt())
                .addValue("bank_id", dto.getBankId())
                .addValue("holder_id", dto.getHolderId())
                .addValue("salary_project_id", dto.getSalaryProjectId())
                .addValue("salary", dto.getSalary());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(SalaryAccount account) {
        var map = getMapSqlParameterSource(account);

        map.addValue("id", account.getId());

        return map;
    }

    @Override
    protected SalaryAccount fromDto(SalaryAccountDto dto) {
        return SalaryAccount.fromDto(dto);
    }
}
