package example.banking.transaction.mapper;

import example.banking.transaction.dto.TransactionDto;
import example.banking.transaction.dto.TransactionResponseDto;
import example.banking.transaction.entity.Transaction;

public class TransactionMapper {

    public static TransactionResponseDto toResponseDto(Transaction t) {
        return toResponseDto(t.toDto());
    }

    public static TransactionResponseDto toResponseDto(TransactionDto d) {
        return new TransactionResponseDto(
                d.getId(), d.getFromEntityId(), d.getToEntityId(), d.getFromType(), d.getToType(), d.getAmount(), d.getTimestamp()
        );
    }
}
