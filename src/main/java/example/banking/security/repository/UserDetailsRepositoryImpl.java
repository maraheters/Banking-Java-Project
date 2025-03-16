package example.banking.security.repository;

import example.banking.security.BankingUserDetails;
import example.banking.security.userDetailsRowMapper.BankingUserDetailsRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDetailsRepositoryImpl implements UserDetailsRepository {

    private final NamedParameterJdbcTemplate template;

    public UserDetailsRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<BankingUserDetails> findByEmail(String email) {
        // 1. First we find user to use their id later
        // 2. Then find their roles either from client roles or supervisor roles
        //    since user may be a client or a supervisor
        // 3. Join user with roles and aggregate roles into a single string of roles separated by commas (',')
        String sql = """
            WITH target_user AS (
                SELECT id, email, password_hash
                FROM public.user
                WHERE email = :email
                LIMIT 1
            ),
            user_roles AS (
                 -- Roles from the client table
                 SELECT
                         c.user_id,
                         cr.name AS role_name
                 FROM client c
                     JOIN target_user tu ON c.user_id = tu.id
                     LEFT JOIN public.client_role_client crc ON c.id = crc.client_id
                     LEFT JOIN public.client_role cr ON cr.id = crc.role_id
        
                 UNION

                 -- Roles from the supervisor table
                 SELECT
                     sup.user_id,
                     sr.name AS role_name
                 FROM supervisor sup
                      JOIN target_user tu ON sup.user_id = tu.id
                      LEFT JOIN public.supervisor_role_supervisor srs ON sup.id = srs.supervisor_id
                      LEFT JOIN public.supervisor_role sr ON sr.id = srs.role_id
         
                UNION      
         
                -- Roles from the specialist table
                SELECT 
                    spec.user_id,
                    'SPECIALIST' AS role_name
                FROM specialist spec
                    JOIN target_user tu ON spec.user_id = tu.id
        
            ),
             client_id AS (
                 SELECT c.id
                 FROM public.client c
                          JOIN target_user tu ON c.user_id = tu.id
             )
            SELECT
                tu.id AS user_id,
                (SELECT id FROM client_id) AS client_id,
                tu.email,
                tu.password_hash,
                STRING_AGG(ur.role_name, ',') AS roles
            FROM target_user tu
                     LEFT JOIN user_roles ur ON ur.user_id = tu.id
            GROUP BY tu.id, tu.email, tu.password_hash;
        """;

        var map = new MapSqlParameterSource();
        map.addValue("email", email);

        try {
            return Optional.ofNullable(
                    template.queryForObject(sql, map, new BankingUserDetailsRowMapper())
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while querying for user with email: '" + email + "': ", e);
        }
    }
}
