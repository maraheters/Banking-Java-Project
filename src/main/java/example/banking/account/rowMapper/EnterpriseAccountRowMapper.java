package example.banking.account.rowMapper;

import example.banking.account.dto.EnterpriseAccountDto;
import example.banking.account.types.AccountStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class EnterpriseAccountRowMapper implements RowMapper<EnterpriseAccountDto> {
    @Override
    public EnterpriseAccountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        var dto = new EnterpriseAccountDto();

        dto.setId(rs.getLong("id"));
        dto.setEnterpriseId(rs.getLong("enterprise_id"));
        dto.setSpecialistId(rs.getLong("specialist_id"));
        dto.setIBAN(rs.getString("iban"));
        dto.setBankId(rs.getLong("bank_id"));
        dto.setStatus(AccountStatus.valueOf(rs.getString("status")));
        dto.setBalance(rs.getBigDecimal("balance"));
        dto.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

        return dto;
    }
}
