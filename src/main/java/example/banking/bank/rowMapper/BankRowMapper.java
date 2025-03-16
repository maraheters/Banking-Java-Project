package example.banking.bank.rowMapper;

import example.banking.bank.dto.BankDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BankRowMapper implements RowMapper<BankDto> {
    @Override
    public BankDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BankDto(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("bic"),
                rs.getString("address")
        );
    }
}
