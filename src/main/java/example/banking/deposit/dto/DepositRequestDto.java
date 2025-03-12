package example.banking.deposit.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequestDto {
    private Long accountId;
    private String termName;
    private BigDecimal amount;
}
