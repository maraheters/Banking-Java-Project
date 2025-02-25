package example.banking.user.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.user.dto.client.ClientDto;
import example.banking.user.entity.Client;
import example.banking.user.rowMapper.PendingClientRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PendingClientsRepositoryImpl
        extends AbstractRepository<Client, ClientDto>
        implements PendingClientsRepository {

    @Autowired
    public PendingClientsRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = new PendingClientRowMapper();
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        String sql = "SELECT * FROM public.pending_client WHERE email = :email";

        var map = new MapSqlParameterSource("email", email);

        try {
            return Optional.of(
                    fromDto(template.queryForObject(sql, map, mapper)));

        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query: ", e);
        }
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
            INSERT INTO public.pending_client (name, email, password_hash, phone_number, passport_number, identification_number)
            VALUES (:name, :email, :password_hash, :phone_number, :passport_number, :identification_number)
            RETURNING id;
        """;
    }

    @Override
    protected String getUpdateSql() {
        return "";
    }

    @Override
    protected String getDeleteSql() {
        return """
            DELETE FROM public.pending_client WHERE id = :id
        """;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Client client) {
        var map = new MapSqlParameterSource();
        var dto = client.toDto();
        map
                .addValue("name", dto.getName())
                .addValue("email", dto.getEmail())
                .addValue("password_hash", dto.getPasswordHash())
                .addValue("phone_number", dto.getPhoneNumber())
                .addValue("passport_number", dto.getPassportNumber())
                .addValue("identification_number", dto.getIdentificationNumber());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Client client) {
        var map = getMapSqlParameterSource(client);
        map.addValue("id", client.getId());
        return map;
    }

    @Override
    protected Client fromDto(ClientDto dto) {
        return Client.fromDto(dto);
    }
}
