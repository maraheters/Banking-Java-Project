package example.banking.enterprise.repository;

import example.banking.enterprise.dto.EnterpriseDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnterpriseRowMapper implements RowMapper<EnterpriseDto> {
    @Override
    public EnterpriseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new EnterpriseDto(
                rs.getLong("id"),
                rs.getLong("bank_id"),
                rs.getString("type"),
                rs.getString("legal_name"),
                rs.getString("unp"),
                rs.getString("legal_address")
        );
    }
}
