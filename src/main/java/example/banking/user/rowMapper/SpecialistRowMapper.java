package example.banking.user.rowMapper;

import example.banking.user.dto.specialist.SpecialistDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecialistRowMapper implements RowMapper<SpecialistDto> {
    @Override
    public SpecialistDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SpecialistDto(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("enterprise_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password_hash")
        );
    }
}
