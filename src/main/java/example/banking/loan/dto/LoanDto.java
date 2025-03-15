package example.banking.loan.dto;

import example.banking.loan.types.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LoanDto {
    private Long id;
    private Long accountId;
    private BigDecimal principalAmount;
    private BigDecimal paidAmount;
    private BigDecimal interestRate;
    private Integer lengthInMonths;
    private LoanStatus status;
    private LocalDateTime createdAt;
    private LocalDate lastPayment;
}
