package example.banking.account.rowMapper;

import example.banking.account.dto.SalaryAccountDto;
import example.banking.account.types.AccountStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SalaryAccountRowMapper implements RowMapper<SalaryAccountDto> {
    @Override
    public SalaryAccountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        var dto = new SalaryAccountDto();

        dto.setId(rs.getLong("id"));
        dto.setHolderId(rs.getLong("holder_id"));
        dto.setSalaryProjectId(rs.getLong("salary_project_id"));
        dto.setIBAN(rs.getString("iban"));
        dto.setBankId(rs.getLong("bank_id"));
        dto.setStatus(AccountStatus.valueOf(rs.getString("status")));
        dto.setBalance(rs.getBigDecimal("balance"));
        dto.setSalary(rs.getBigDecimal("salary"));
        dto.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

        return dto;
    }
}
