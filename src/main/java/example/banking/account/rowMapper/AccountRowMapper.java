package example.banking.account.rowMapper;


import example.banking.account.dto.PersonalAccountDto;
import example.banking.account.types.AccountStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AccountRowMapper implements RowMapper<PersonalAccountDto> {

    @Override
    public PersonalAccountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PersonalAccountDto(
                rs.getLong("id"),
                rs.getLong("holder_id"),
                rs.getString("iban"),
                rs.getBigDecimal("balance"),
                AccountStatus.valueOf(rs.getString("status")),
                rs.getLong("bank_id"),
                rs.getObject("created_at", LocalDateTime.class)
        );
    }
}
