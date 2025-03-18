package example.banking.salary.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalaryAccountRequestDto {
    private Long holderId;
    private BigDecimal salary;
}
