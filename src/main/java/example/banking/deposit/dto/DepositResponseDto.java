package example.banking.deposit.dto;

import example.banking.deposit.types.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DepositResponseDto {
    private Long id;
    private BigDecimal minimum;
    private BigDecimal bonus;
    private DepositStatus status;
    private LocalDateTime createdAt;
    private LocalDate lastBonusDate;
    private Integer numberOfBonusesYet;  // Used to track how many bonuses have been applied
    private Integer lengthInMonths;
    private Long accountId;
    private BigDecimal interestRate;
}
