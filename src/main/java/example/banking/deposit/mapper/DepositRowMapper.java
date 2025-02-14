package example.banking.deposit.mapper;

import example.banking.deposit.entity.Deposit;
import example.banking.deposit.types.DepositStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepositRowMapper implements RowMapper<Deposit> {

    @Override
    public Deposit mapRow(ResultSet rs, int rowNum) throws SQLException {
        Deposit deposit = new Deposit();

        deposit.setId(rs.getLong("id"));
        deposit.setInterestRate(rs.getDouble("interest_rate"));
        deposit.setAccountId(rs.getLong("account_id"));
        deposit.setBalance(rs.getBigDecimal("balance"));
        deposit.setDateCreated(rs.getDate("date_created").toLocalDate());
        deposit.setAccountId(rs.getLong("account_id"));
        deposit.setStatus(DepositStatus.valueOf(rs.getString("status")));
        deposit.setDateCreated(null);

        return deposit;
    }
}
