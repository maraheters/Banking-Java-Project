package example.banking.transaction.rowMapper;

import example.banking.transaction.dto.TransactionDto;
import example.banking.transaction.types.TransactionType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TransactionRowMapper implements RowMapper<TransactionDto> {

    @Override
    public TransactionDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new TransactionDto(
                rs.getLong("id"),
                rs.getLong("from_entity_id"),
                rs.getLong("to_entity_id"),
                TransactionType.valueOf(rs.getString("from_type")),
                TransactionType.valueOf(rs.getString("to_type")),
                rs.getBigDecimal("amount"),
                rs.getObject("timestamp", LocalDateTime.class)
        );

    }
}
