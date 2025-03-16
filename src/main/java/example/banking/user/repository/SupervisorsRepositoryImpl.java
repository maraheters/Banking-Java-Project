package example.banking.user.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.user.dto.supervisor.SupervisorDto;
import example.banking.user.entity.Supervisor;
import example.banking.user.rowMapper.SupervisorRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SupervisorsRepositoryImpl
        extends AbstractRepository<Supervisor, SupervisorDto>
        implements SupervisorsRepository {

    @Autowired
    public SupervisorsRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    protected RowMapper<SupervisorDto> getRowMapper() {
        return new SupervisorRowMapper();
    }

    @Override
    protected String getFindAllSql() {
        return """
                SELECT
                    s.id,
                    u.name,
                    u.email,
                    u.password_hash,
                    STRING_AGG(sr.name, ',') AS roles
                FROM supervisor s
                    JOIN public.user u ON u.id = s.id
                    LEFT JOIN public.supervisor_role_supervisor srs ON s.id = srs.supervisor_id
                    LEFT JOIN public.supervisor_role sr ON sr.id = srs.role_id
                GROUP BY s.id, u.id
        """;
    }

    @Override
    protected String getFindByIdSql() {
        return """
                SELECT
                    s.id,
                    u.name,
                    u.email,
                    u.password_hash,
                    STRING_AGG(sr.name, ',') AS roles
                FROM supervisor s
                         JOIN public.user u ON u.id = s.id
                         LEFT JOIN public.supervisor_role_supervisor srs ON s.id = srs.supervisor_id
                         LEFT JOIN public.supervisor_role sr ON sr.id = srs.role_id
                WHERE s.id = :id
                GROUP BY s.id, u.id
        """;
    }

    @Override
    protected String getCreateSql() {
        // 1. Insert information into user table, common for both clients and supervisor
        // 2. Insert information into supervisor table
        // 3. Find role ids corresponding to supervisor's roles, represented as an array of strings
        // 4. Insert those ids and supervisor's id into join table
        // 5. Return supervisor's generated id
        return """
            WITH inserted_user AS (
                INSERT INTO public.user(name, email, password_hash)
                VALUES (:name, :email, :password_hash)
                RETURNING id
            ),
            inserted_supervisor AS (
                INSERT INTO public.supervisor(id)
                VALUES ((SELECT id FROM inserted_user))
                RETURNING id
            ),
            role_ids AS (
                SELECT id
                FROM public.supervisor_role
                WHERE name IN (:role_names)
            ),
            id AS (
                INSERT INTO public.supervisor_role_supervisor(role_id, supervisor_id)
                SELECT id, (SELECT id FROM inserted_supervisor)
                FROM role_ids
                RETURNING supervisor_id
            )
            SELECT * FROM id LIMIT 1;
        """;
    }

    @Override
    protected String getUpdateSql() {
        return "";
    }

    @Override
    protected String getDeleteSql() {
        return "";
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Supervisor supervisor) {
        var d = supervisor.toDto();
        var map = new MapSqlParameterSource();
        map
                .addValue("name", d.getName())
                .addValue("email", d.getEmail())
                .addValue("password_hash", d.getPasswordHash())
                .addValue("role_names", d.getRoles().stream().map(Enum::toString).toList());
        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Supervisor supervisor) {
        var map = getMapSqlParameterSource(supervisor);
        map.addValue("id", supervisor.getId());
        return map;
    }

    @Override
    protected Supervisor fromDto(SupervisorDto dto) {
        return Supervisor.fromDto(dto);
    }
}
