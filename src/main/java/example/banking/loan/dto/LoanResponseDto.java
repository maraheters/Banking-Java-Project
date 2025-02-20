package example.banking.loan.dto;

import example.banking.loan.types.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LoanResponseDto {
    private Long id;
    private Long accountId;
    private BigDecimal initialAmount;
    private BigDecimal paidAmount;
    private BigDecimal interestRate;
    private Integer lengthInMonths;
    private LoanStatus status;
    private LocalDate createdAt;
    private LocalDate lastPayment;
}
