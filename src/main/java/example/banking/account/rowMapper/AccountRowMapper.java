package example.banking.account.rowMapper;


import example.banking.account.dto.AccountDto;
import example.banking.account.types.AccountStatus;
import example.banking.account.types.AccountType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                rs.getDate("date_created").toLocalDate().atStartOfDay());
    }
}
