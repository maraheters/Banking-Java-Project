package example.banking.account.rowMapper;

import example.banking.account.dto.AccountDto;
import example.banking.account.types.AccountStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GeneralAccountRowMapper implements RowMapper<AccountDto> {

    @Override
    public AccountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        var dto = new AccountDto();

        dto.setId(rs.getLong("id"));
        dto.setIBAN(rs.getString("iban"));
        dto.setBankId(rs.getLong("bank_id"));
        dto.setStatus(AccountStatus.valueOf(rs.getString("status")));
        dto.setBalance(rs.getBigDecimal("balance"));
        dto.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

        return dto;
    }
}
