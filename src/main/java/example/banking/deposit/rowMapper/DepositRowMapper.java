package example.banking.deposit.rowMapper;

import example.banking.deposit.dto.DepositDto;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.types.DepositStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepositRowMapper implements RowMapper<Deposit> {

    @Override
    public Deposit mapRow(ResultSet rs, int rowNum) throws SQLException {

        var rsBonusDate = rs.getDate("last_bonus_date");
        var rsDateCreated = rs.getDate("date_created");

        var lastBonusDate = rsBonusDate != null
                ? rsBonusDate.toLocalDate()
                : null;

        var dateCreated = rsDateCreated != null
                ? rsDateCreated.toLocalDate()
                : null;

        var depositDto = new DepositDto(
                rs.getLong("id"),
                rs.getBigDecimal("minimum"),
                rs.getBigDecimal("bonus"),
                DepositStatus.valueOf(rs.getString("status")),
                dateCreated,
                lastBonusDate,
                rs.getInt("number_of_bonuses"),
                rs.getInt("length_in_months"),
                rs.getLong("account_id"),
                rs.getDouble("interest_rate"));

        return Deposit.fromDto(depositDto);
    }
}
