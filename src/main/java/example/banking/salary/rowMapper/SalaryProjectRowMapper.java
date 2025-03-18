package example.banking.salary.rowMapper;

import example.banking.salary.dto.SalaryProjectDto;

import example.banking.salary.types.SalaryProjectStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SalaryProjectRowMapper implements RowMapper<SalaryProjectDto> {

    @Override
    public SalaryProjectDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SalaryProjectDto(
                rs.getLong("id"),
                rs.getLong("account_id"),
                rs.getLong("enterprise_id"),
                rs.getLong("specialist_id"),
                rs.getObject("created_at", LocalDateTime.class),
                rs.getBigDecimal("total_salary"),
                SalaryProjectStatus.valueOf(rs.getString("status"))
        );
    }
}
