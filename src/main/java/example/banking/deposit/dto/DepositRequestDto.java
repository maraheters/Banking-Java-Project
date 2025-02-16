package example.banking.deposit.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequestDto {
    private Long accountId;
    private double interestRate;
    private int lengthInMonths;
    private BigDecimal initialBalance;
}
