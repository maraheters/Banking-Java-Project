package example.banking.account.dto;

import example.banking.account.types.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryAccountResponseDto {
    private Long id;
    private Long bankId;
    private Long holderId;
    private Long salaryProjectId;
    private String IBAN;
    private BigDecimal balance;
    private BigDecimal salary;
    private AccountStatus status;
    private LocalDateTime createdAt;
}
