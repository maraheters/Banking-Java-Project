package example.banking.user.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.user.dto.client.ClientDto;
import example.banking.user.entity.Client;
import example.banking.user.rowMapper.ClientRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClientsRepositoryImpl
        extends AbstractRepository<Client, ClientDto>
        implements ClientsRepository {

    public ClientsRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = new ClientRowMapper();
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        String sql = """
                SELECT
                    c.id AS client_id,
                    c.user_id,
                    c.identification_number,
                    c.phone_number,
                    c.passport_number,
                    u.id AS user_id,
                    u.name,
                    u.email,
                    u.password_hash,
                    STRING_AGG(cr.name, ',') AS roles
                FROM client c
                         JOIN public.user u ON u.id = c.user_id
                         LEFT JOIN public.client_role_client crc ON c.id = crc.client_id
                         LEFT JOIN public.client_role cr ON cr.id = crc.role_id
                WHERE u.email = :email
                GROUP BY c.id, u.id
        """;

        var map = new MapSqlParameterSource("email", email);

        return findByCriteria(sql, map);
    }

    @Override
    protected String getCreateSql() {
        // 1. Insert information into user table, common for both clients and supervisor
        // 2. Insert information into client table
        // 3. Find role ids corresponding to client's roles, represented as an array of strings
        // 4. Insert those ids and client's id into join table
        // 5. Return client's generated id
        return """
                    WITH inserted_user AS (
                        INSERT INTO public.user(name, email, password_hash)
                        VALUES (:name, :email, :password_hash)
                        RETURNING id
                    ),
                    inserted_client AS (
                        INSERT INTO public.client (user_id, phone_number, passport_number, identification_number)
                        VALUES ((SELECT id FROM inserted_user), :phone_number, :passport_number, :identification_number)
                        RETURNING id
                    ),
                    role_ids AS (
                        SELECT id
                        FROM public.client_role
                        WHERE name IN (:role_names)
                    ),
                    id AS (
                        INSERT INTO public.client_role_client(role_id, client_id)
                        SELECT id, (SELECT id FROM inserted_client)
                        FROM role_ids
                        RETURNING client_id
                    )
                    SELECT * FROM id LIMIT 1;
                """;
    }

    @Override
    protected String getUpdateSql() {
        return """
                WITH
                updated_user AS (
                    UPDATE public.user
                    SET name = :name,
                        email = :email,
                        password_hash = :password_hash
                    WHERE id = (
                        SELECT user_id FROM public.client WHERE id = :id
                    )
                    RETURNING id
                ),
                updated_client AS (
                    UPDATE public.client
                    SET phone_number = :phone_number,
                        passport_number = :passport_number,
                        identification_number = :identification_number
                    WHERE id = :id
                    RETURNING id
                ),
                role_ids AS (
                    SELECT id
                    FROM public.client_role
                    WHERE name IN (:role_names)
                ),
                delete_unused AS (
                    -- Remove role associations that are no longer desired.
                    DELETE FROM public.client_role_client
                    WHERE client_id = :id
                      AND role_id NOT IN (SELECT id FROM role_ids)
                    RETURNING client_id
                )
                -- Insert new role associations if they don't already exist.
                INSERT INTO public.client_role_client (client_id, role_id)
                SELECT :id, id
                FROM role_ids
                ON CONFLICT (client_id, role_id) DO NOTHING;
        """;
    }

    @Override
    protected String getDeleteSql() {
        return "";
    }

    @Override
    protected String getFindByIdSql() {
        return """
                SELECT
                    c.id AS client_id,
                    c.user_id,
                    c.identification_number,
                    c.phone_number,
                    c.passport_number,
                    u.id AS user_id,
                    u.name,
                    u.email,
                    u.password_hash,
                    STRING_AGG(cr.name, ',') AS roles
                FROM client c
                         JOIN public.user u ON u.id = c.user_id
                         LEFT JOIN public.client_role_client crc ON c.id = crc.client_id
                         LEFT JOIN public.client_role cr ON cr.id = crc.role_id
                WHERE c.id = :id
                GROUP BY c.id, u.id
        """;
    }
    @Override
    protected String getFindAllSql() {
        return """
                SELECT
                    c.id AS client_id,
                    c.user_id,
                    c.identification_number,
                    c.phone_number,
                    c.passport_number,
                    u.id AS user_id,
                    u.name,
                    u.email,
                    u.password_hash,
                    STRING_AGG(cr.name, ',') AS roles
                FROM client c
                         JOIN public.user u ON u.id = c.user_id
                         LEFT JOIN public.client_role_client crc ON c.id = crc.client_id
                         LEFT JOIN public.client_role cr ON cr.id = crc.role_id
                GROUP BY c.id, u.id
        """;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Client client) {
        var map = new MapSqlParameterSource();
        var d = client.toDto();
        map
                .addValue("name", d.getName())
                .addValue("email", d.getEmail())
                .addValue("password_hash", d.getPasswordHash())
                .addValue("phone_number", d.getPhoneNumber())
                .addValue("passport_number", d.getPassportNumber())
                .addValue("identification_number", d.getIdentificationNumber())
                .addValue("role_names", d.getRoles().stream().map(Enum::toString).toList());

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
