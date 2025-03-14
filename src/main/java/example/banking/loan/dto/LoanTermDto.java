package example.banking.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanTermDto {
    private String name;
    private BigDecimal interestRate;
    private Integer months;
}
