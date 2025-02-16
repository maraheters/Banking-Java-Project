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
        deposit.setMinimum(rs.getBigDecimal("balance"));
        deposit.setAccountId(rs.getLong("account_id"));
        deposit.setStatus(DepositStatus.valueOf(rs.getString("status")));
        deposit.setNumberOfBonusesYet(rs.getInt("number_of_bonuses"));

        var lastBonusDate = rs.getDate("last_bonus");
        if (lastBonusDate != null) {
            deposit.setLastBonusDate(lastBonusDate.toLocalDate());
        }

        var dateCreated = rs.getDate("date_created");
        if (dateCreated != null) {
            deposit.setDateCreated(dateCreated.toLocalDate());
        }


        return deposit;
    }
}
