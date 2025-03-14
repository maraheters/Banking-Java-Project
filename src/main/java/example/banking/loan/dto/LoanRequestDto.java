package example.banking.loan.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequestDto {
    private String termName;
    private BigDecimal amount;
    private Long accountId;
}
