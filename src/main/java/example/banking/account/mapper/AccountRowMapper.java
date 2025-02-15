package example.banking.account.mapper;


import example.banking.account.entity.Account;
import example.banking.account.types.AccountStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        var account = new Account();

        account.setId(rs.getLong("id"));
        account.setIBAN(rs.getString("iban"));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setDateCreated(rs.getDate("date_created").toLocalDate().atStartOfDay());
        account.setHolderId(rs.getLong("holder_id"));

        return account;
    }
}
