package example.banking.salary.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.salary.dto.SalaryProjectDto;
import example.banking.salary.model.SalaryProject;
import example.banking.salary.rowMapper.SalaryProjectRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalaryProjectRepositoryImpl
    extends AbstractRepository<SalaryProject, SalaryProjectDto>
    implements SalaryProjectRepository {

    @Autowired
    public SalaryProjectRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    protected RowMapper<SalaryProjectDto> getRowMapper() {
        return new SalaryProjectRowMapper();
    }

    @Override
    public List<SalaryProject> findAllByEnterpriseId(Long id) {
        String sql = """
                SELECT * FROM salary_project_view
                WHERE enterprise_id = :enterprise_id
        """;

        var map = new MapSqlParameterSource("enterprise_id", id);

        return findAllByCriteria(sql, map);
    }

    @Override
    public List<SalaryProject> findAllBySpecialistId(Long id) {
        String sql = """
                SELECT * FROM salary_project_view
                WHERE specialist_id = :specialist_id
        """;

        var map = new MapSqlParameterSource("specialist_id", id);

        return findAllByCriteria(sql, map);
    }

    @Override
    public List<SalaryProject> findAllByAccountId(Long id) {
        String sql = """
                SELECT * FROM salary_project_view
                WHERE account_id = :account_id
        """;

        var map = new MapSqlParameterSource("account_id", id);

        return findAllByCriteria(sql, map);
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM salary_project_view";
    }

    @Override
    protected String getFindByIdSql() {
        return "SELECT * FROM salary_project_view where id = :id";
    }

    @Override
    protected String getCreateSql() {
        return """
            INSERT INTO salary_project(enterprise_id, account_id, created_at, status)
            VALUES (:enterprise_id, :account_id, :created_at, :status)
            RETURNING id
        """;
    }

    @Override
    protected String getUpdateSql() {
        return """
            UPDATE salary_project
            SET
                enterprise_id = :enterprise_id,
                account_id = :account_id,
                created_at = :created_at,
                status = :status
            WHERE id = :id
        """;
    }

    @Override
    protected String getDeleteSql() {
        return """
            DELETE FROM salary_project WHERE id = :id
        """;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(SalaryProject salaryProject) {
        var map = new MapSqlParameterSource();
        var dto = salaryProject.toDto();

        map
                .addValue("enterprise_id", dto.getEnterpriseId())
                .addValue("account_id", dto.getAccountId())
                .addValue("created_at", dto.getCreatedAt())
                .addValue("status", dto.getStatus().toString());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(SalaryProject salaryProject) {
        var map = getMapSqlParameterSource(salaryProject);
        map.addValue("id", salaryProject.getId());

        return map;
    }

    @Override
    protected SalaryProject fromDto(SalaryProjectDto dto) {
        return SalaryProject.fromDto(dto);
    }
}
