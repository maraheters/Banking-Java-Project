package example.banking.transaction.entity;

import example.banking.exception.BadRequestException;
import example.banking.transaction.dto.TransactionDto;
import example.banking.transaction.types.TransactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transaction {
    private Long id;
    private Long fromEntityId;
    private Long toEntityId;
    @Getter
    private Long revertTransactionId;
    private TransactionType fromType;
    private TransactionType toType;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public static Transaction create(
            Long fromEntityId, TransactionType fromType, Long toEntityId, TransactionType toType, BigDecimal amount) {

        return new Transaction(
                null, fromEntityId, toEntityId, null, fromType, toType, amount, LocalDateTime.now()
        );
    }

    public TransactionDto toDto() {
        return new TransactionDto(id, fromEntityId, toEntityId, revertTransactionId, fromType, toType, amount, timestamp);
    }

    public static Transaction fromDto(TransactionDto d) {
        return new Transaction(
                d.getId(),
                d.getFromEntityId(),
                d.getToEntityId(),
                d.getRevertTransactionId(),
                d.getFromType(),
                d.getToType(),
                d.getAmount(),
                d.getTimestamp());
    }

    public void setRevertTransactionId(Long id) {
        if (revertTransactionId != null)
            throw new BadRequestException("Transaction has already been reversed");

        revertTransactionId = id;
    }
}
