package example.banking.user.rowMapper;

import example.banking.user.dto.supervisor.SupervisorDto;
import example.banking.user.roles.SupervisorRole;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SupervisorRowMapper implements RowMapper<SupervisorDto> {

    @Override
    public SupervisorDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        var supervisor = new SupervisorDto();
        supervisor.setId(rs.getLong("id"));
        supervisor.setName(rs.getString("name"));
        supervisor.setEmail(rs.getString("email"));
        supervisor.setPasswordHash(rs.getString("password_hash"));
        supervisor.setRoles(
                Arrays.stream(rs.getString("roles").split(","))
                        .map(SupervisorRole::valueOf)
                        .toList());
        return supervisor;
    }
}
