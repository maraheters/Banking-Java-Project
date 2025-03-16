package example.banking.user.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.user.dto.client.PendingClientDto;
import example.banking.user.entity.PendingClient;
import example.banking.user.rowMapper.PendingClientRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PendingClientsRepositoryImpl
        extends AbstractRepository<PendingClient, PendingClientDto>
        implements PendingClientsRepository {

    @Autowired
    public PendingClientsRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    protected RowMapper<PendingClientDto> getRowMapper() {
        return new PendingClientRowMapper();
    }

    @Override
    public Optional<PendingClient> findByEmail(String email) {
        String sql = "SELECT * FROM public.pending_client WHERE email = :email";

        var map = new MapSqlParameterSource("email", email);

        return findByCriteria(sql, map);
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM public.pending_client";
    }

    @Override
    protected String getFindByIdSql() {
        return "SELECT * FROM public.pending_client WHERE id = :id";
    }

    @Override
    protected String getCreateSql() {
        return """
            INSERT INTO public.pending_client
                (name, email, password_hash, phone_number, passport_number, identification_number, requested_at, status)
            VALUES
                (:name, :email, :password_hash, :phone_number, :passport_number, :identification_number, :requested_at, :status)
            RETURNING id;
        """;
    }

    @Override
    protected String getUpdateSql() {
        return """
            UPDATE public.pending_client
            SET
                name = :name,
                email = :email,
                password_hash = :password_hash,
                phone_number = :phone_number,
                passport_number = :passport_number,
                identification_number = :identification_number,
                requested_at = :requested_at,
                status = :status
            WHERE id = :id
        """;
    }

    @Override
    protected String getDeleteSql() {
        return """
            DELETE FROM public.pending_client WHERE id = :id
        """;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(PendingClient client) {
        var map = new MapSqlParameterSource();
        var dto = client.toDto();
        map
                .addValue("name", dto.getName())
                .addValue("email", dto.getEmail())
                .addValue("password_hash", dto.getPasswordHash())
                .addValue("phone_number", dto.getPhoneNumber())
                .addValue("passport_number", dto.getPassportNumber())
                .addValue("identification_number", dto.getIdentificationNumber())
                .addValue("requested_at", dto.getRequestedAt())
                .addValue("status", dto.getStatus().toString());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(PendingClient client) {
        var map = getMapSqlParameterSource(client);
        map.addValue("id", client.getId());
        return map;
    }

    @Override
    protected PendingClient fromDto(PendingClientDto dto) {
        return PendingClient.fromDto(dto);
    }
}
