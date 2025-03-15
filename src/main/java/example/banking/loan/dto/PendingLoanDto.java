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
public class PendingLoanDto {
    private Long id;
    private Long accountId;
    private BigDecimal principalAmount;
    private BigDecimal interestRate; //in decimal form, eg 0.08 = 8%
    private Integer lengthInMonths;
    private PendingEntityStatus status;
    private LocalDateTime requestedAt;
}
