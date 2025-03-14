package example.banking.user.rowMapper;

import example.banking.contracts.PendingEntityStatus;
import example.banking.user.dto.client.PendingClientDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PendingClientRowMapper implements RowMapper<PendingClientDto> {

    @Override
    public PendingClientDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PendingClientDto(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password_hash"),
            rs.getString("phone_number"),
            rs.getString("passport_number"),
            rs.getString("identification_number"),
            rs.getDate("requested_at").toLocalDate(),
            PendingEntityStatus.valueOf(rs.getString("status"))
        );
    }
}
