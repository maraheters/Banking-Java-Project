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
        deposit.setLengthInMonths(rs.getInt("length_in_months"));
        deposit.setStatus(DepositStatus.valueOf(rs.getString("status")));
        deposit.setNumberOfBonusesYet(rs.getInt("number_of_bonuses"));
        deposit.setBonus(rs.getBigDecimal("bonus"));
        deposit.setMinimum(rs.getBigDecimal("minimum"));

        var lastBonusDate = rs.getDate("last_bonus_date");
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
