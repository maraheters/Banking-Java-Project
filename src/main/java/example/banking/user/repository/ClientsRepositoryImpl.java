package example.banking.user.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.user.dto.client.ClientDto;
import example.banking.user.entity.Client;
import example.banking.user.rowMapper.ClientRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClientsRepositoryImpl extends AbstractRepository<Client, ClientDto> implements ClientsRepository {

    public ClientsRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = new ClientRowMapper();
    }

    @Override
    protected String getCreateSql() {
        return """
                    WITH inserted_user AS (
                        INSERT INTO public.user(name, email, password_hash)
                        VALUES (:name, :email, :password_hash)
                        RETURNING id
                    )
                    INSERT INTO public.client(user_id, phone_number, passport_number, identification_number)
                    VALUES ((SELECT id FROM inserted_user), :phone_number, :passport_number, :identification_number)
                    RETURNING id;
                """;
    }

    @Override
    protected String getUpdateSql() {
        return "";
    }

    @Override
    protected String getRemoveSql() {
        return "";
    }

    @Override
    protected String getFindByIdSql() {
        return """
                    SELECT
                        c.id AS client_id,
                        c.user_id,
                        c.phone_number,
                        c.passport_number,
                        c.identification_number,
                        u.id AS user_id,
                        u.name,
                        u.email,
                        u.password_hash
                    FROM client c
                    JOIN public.user u ON u.id = c.user_id
                    WHERE c.id = :id
                """;
    }
    @Override
    protected String getFindAllSql() {
        return """
                    SELECT
                        c.id AS client_id,
                        c.user_id,
                        c.phone_number,
                        c.passport_number,
                        c.identification_number,
                        u.id AS user_id,
                        u.name,
                        u.email,
                        u.password_hash
                    FROM client c
                    JOIN public.user u ON u.id = c.user_id
                """;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Client client) {
        var map = new MapSqlParameterSource();
        var d = client.toDto();
        map.addValue("name", d.getName());
        map.addValue("email", d.getEmail());
        map.addValue("password_hash", d.getPasswordHash());
        map.addValue("phone_number", d.getPhoneNumber());
        map.addValue("passport_number", d.getPassportNumber());
        map.addValue("identification_number", d.getIdentificationNumber());

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
