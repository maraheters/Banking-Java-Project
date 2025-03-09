package example.banking.transaction.entity;

import example.banking.transaction.dto.TransactionDto;
import example.banking.transaction.types.TransactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transaction {
    @Getter
    private Long id;
    private Long fromEntityId;
    private Long toEntityId;
    private TransactionType fromType;
    private TransactionType toType;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public static Transaction create(
            Long fromEntityId, TransactionType fromType, Long toEntityId, TransactionType toType, BigDecimal amount) {

        return new Transaction(
                null, fromEntityId, toEntityId, fromType, toType, amount, LocalDateTime.now()
        );
    }

    public TransactionDto toDto() {
        return new TransactionDto(id, fromEntityId, toEntityId, fromType, toType, amount, timestamp);
    }

    public static Transaction fromDto(TransactionDto d) {
        return new Transaction(
                d.getId(),
                d.getFromEntityId(),
                d.getToEntityId(),
                d.getFromType(),
                d.getToType(),
                d.getAmount(),
                d.getTimestamp());
    }
}
