package example.banking.security.userDetailsRowMapper;

import example.banking.security.BankingUserDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class BankingUserDetailsRowMapper implements RowMapper<BankingUserDetails> {
    @Override
    public BankingUserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        var id = rs.getLong("id");
        var email = rs.getString("email");
        var passwordHash = rs.getString("password_hash");
        var roles = Arrays.stream(rs.getString("roles").split(",")).toList();

        return new BankingUserDetails(id, email, passwordHash, roles);
    }
}
