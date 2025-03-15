package example.banking.account.rowMapper;


import example.banking.account.dto.AccountDto;
import example.banking.account.types.AccountStatus;
import example.banking.account.types.AccountType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AccountRowMapper implements RowMapper<AccountDto> {

    @Override
    public AccountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AccountDto(
                rs.getLong("id"),
                rs.getString("iban"),
                rs.getBigDecimal("balance"),
                AccountStatus.valueOf(rs.getString("status")),
                AccountType.valueOf(rs.getString("type")),
                rs.getLong("holder_id"),
                rs.getLong("bank_id"),
                rs.getObject("date_created", LocalDateTime.class)
        );
    }
}
