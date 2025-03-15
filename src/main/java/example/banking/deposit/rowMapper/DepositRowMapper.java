package example.banking.deposit.rowMapper;

import example.banking.deposit.dto.DepositDto;
import example.banking.deposit.types.DepositStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DepositRowMapper implements RowMapper<DepositDto> {

    @Override
    public DepositDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new DepositDto(
                rs.getLong("id"),
                rs.getBigDecimal("minimum"),
                rs.getBigDecimal("bonus"),
                DepositStatus.valueOf(rs.getString("status")),
                rs.getObject("date_created", LocalDateTime.class),
                rs.getObject("last_bonus_date", LocalDate.class),
                rs.getInt("number_of_bonuses"),
                rs.getInt("length_in_months"),
                rs.getLong("account_id"),
                rs.getBigDecimal("interest_rate"));
    }
}
