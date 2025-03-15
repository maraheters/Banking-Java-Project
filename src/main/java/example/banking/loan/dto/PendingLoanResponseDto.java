package example.banking.loan.dto;

import example.banking.contracts.PendingEntityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingLoanResponseDto {
    private Long id;
    private Long accountId;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer lengthInMonths;
    private PendingEntityStatus status;
    private LocalDateTime requestedAt;
}
