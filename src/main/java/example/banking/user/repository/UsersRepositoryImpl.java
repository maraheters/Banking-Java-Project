package example.banking.user.repository;

import example.banking.user.entity.User;
import example.banking.user.mapper.UserRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    private final NamedParameterJdbcTemplate template;
    private final UserRowMapper mapper = new UserRowMapper();

    public UsersRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Long create(User user) {
        String sql = "INSERT INTO banking_user(name, phone_number, passport_number, identification_number, email)  " +
                     "VALUES (:name, :phone_number, :passport_number, :identification_number, :email) RETURNING id";

        var map = new MapSqlParameterSource();
        map.addValue("name", user.getName());
        map.addValue("phone_number", user.getPhoneNumber());
        map.addValue("passport_number", user.getPassportNumber());
        map.addValue("identification_number", user.getIdentificationNumber());
        map.addValue("email", user.getEmail());

        return template.queryForObject(sql, map, Long.class);
    }

    @Override
    public Optional<User> findById(long id) {
        String sql = "SELECT * FROM banking_user WHERE id = :id";
        var parameterSource = new MapSqlParameterSource("id", id);

        try {
            return Optional.ofNullable(template.queryForObject(sql, parameterSource, mapper));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();

        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for user with id '" + id + "'.", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM banking_user";

        try {
            return template.query(sql, mapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for users:", e);
        }
    }
}
