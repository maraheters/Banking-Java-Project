package example.banking.user.rowMapper;

import example.banking.user.dto.client.ClientDto;
import example.banking.user.roles.ClientRole;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class PendingClientRowMapper implements RowMapper<ClientDto> {

    @Override
    public ClientDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        var client = new ClientDto();

        client.setId(rs.getLong("id"));
        client.setUserId(null);
        client.setName(rs.getString("name"));
        client.setEmail(rs.getString("email"));
        client.setPasswordHash(rs.getString("password_hash"));
        client.setPhoneNumber(rs.getString("phone_number"));
        client.setPassportNumber(rs.getString("passport_number"));
        client.setIdentificationNumber(rs.getString("identification_number"));
        client.setRoles(null);

        return client;
    }
}
