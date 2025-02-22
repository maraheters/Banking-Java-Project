package example.banking.loan.rowMapper;

import example.banking.loan.dto.LoanDto;
import example.banking.loan.types.LoanStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanRowMapper implements RowMapper<LoanDto> {

    @Override
    public LoanDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new LoanDto(
                rs.getLong("id"),
                rs.getLong("account_id"),
                rs.getBigDecimal("principal_amount"),
                rs.getBigDecimal("paid_amount"),
                rs.getBigDecimal("interest_rate"),
                rs.getInt("length_in_months"),
                LoanStatus.valueOf(rs.getString("status")),
                rs.getDate("created_at").toLocalDate(),
                rs.getDate("last_payment").toLocalDate());
    }
}
