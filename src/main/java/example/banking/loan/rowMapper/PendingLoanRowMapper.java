package example.banking.loan.rowMapper;

import example.banking.contracts.PendingEntityStatus;
import example.banking.loan.dto.PendingLoanDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PendingLoanRowMapper implements RowMapper<PendingLoanDto> {

    @Override
    public PendingLoanDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PendingLoanDto(
                rs.getLong("id"),
                rs.getLong("account_id"),
                rs.getBigDecimal("principal_amount"),
                rs.getBigDecimal("interest_rate"),
                rs.getInt("length_in_months"),
                PendingEntityStatus.valueOf(rs.getString("status")),
                rs.getDate("requested_at").toLocalDate()
        );

    }
}
