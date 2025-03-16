package example.banking.user.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.user.dto.specialist.SpecialistDto;
import example.banking.user.entity.Specialist;
import example.banking.user.rowMapper.SpecialistRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpecialistsRepositoryImpl
    extends AbstractRepository<Specialist, SpecialistDto>
    implements SpecialistsRepository {

    public SpecialistsRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    protected RowMapper<SpecialistDto> getRowMapper() {
        return new SpecialistRowMapper();
    }

    @Override
    public Optional<Specialist> findByEmail(String email) {
        String sql = """
                SELECT
                    s.id,
                    s.enterprise_id,
                    u.name,
                    u.email,
                    u.password_hash
                FROM specialist s
                JOIN public.user u ON u.id = s.id
                WHERE u.email = :email
        """;

        var map = new MapSqlParameterSource("email", email);

        return findByCriteria(sql, map);
    }

    @Override
    protected String getFindAllSql() {
        return """
                SELECT
                    s.id,
                    s.enterprise_id,
                    u.name,
                    u.email,
                    u.password_hash
                FROM specialist s
                JOIN public.user u ON u.id = s.id
        """;
    }

    @Override
    protected String getFindByIdSql() {
        return """
                SELECT
                    s.id,
                    s.enterprise_id,
                    u.name,
                    u.email,
                    u.password_hash
                FROM specialist s
                JOIN public.user u ON u.id = s.id
                WHERE s.id = :id
        """;
    }

    @Override
    protected String getCreateSql() {
        return """
            WITH inserted_user AS (
                INSERT INTO public.user(name, email, password_hash)
                VALUES (:name, :email, :password_hash)
                RETURNING id
            )
            INSERT INTO public.specialist (id, enterprise_id)
            VALUES ( (SELECT id FROM inserted_user), :enterprise_id )
            RETURNING id
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
    protected MapSqlParameterSource getMapSqlParameterSource(Specialist specialist) {
        var map = new MapSqlParameterSource();
        var dto = specialist.toDto();

        map
                .addValue("name", dto.getName())
                .addValue("email", dto.getEmail())
                .addValue("password_hash", dto.getPasswordHash())
                .addValue("enterprise_id", dto.getEnterpriseId());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Specialist specialist) {
        var map = getMapSqlParameterSource(specialist);
        map.addValue("id", specialist.getId());

        return map;
    }

    @Override
    protected Specialist fromDto(SpecialistDto dto) {
        return Specialist.fromDto(dto);
    }
}
