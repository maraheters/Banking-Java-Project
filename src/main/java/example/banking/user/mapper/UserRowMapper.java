package example.banking.user.mapper;

import example.banking.user.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {


    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        var user = new User();

        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setIdentificationNumber(rs.getString("identification_number"));
        user.setPassportNumber(rs.getString("passport_number"));

        return user;
    }
}
