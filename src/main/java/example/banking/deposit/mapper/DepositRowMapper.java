package example.banking.deposit.mapper;

import example.banking.deposit.entity.Deposit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DepositRowMapper implements RowMapper<Deposit> {

    @Override
    public Deposit mapRow(ResultSet rs, int rowNum) throws SQLException {
        Deposit deposit = new Deposit();

        deposit.setId(rs.getLong("id"));
        deposit.setInterestRate(rs.getDouble("interest_rate"));
//        deposit.setAccount(rs.getObject("account_id", Account.class));
//        deposit.setStatus(rs.getObject("status", DepositStatus.class));
        deposit.setBalance(rs.getBigDecimal("balance"));
        deposit.setDateCreated(rs.getObject("date_created", LocalDateTime.class));
        deposit.setAccountId(rs.getLong("account_id"));
        deposit.setStatus(null);
        deposit.setBalance(null);
        deposit.setDateCreated(null);

        return deposit;
    }
}
