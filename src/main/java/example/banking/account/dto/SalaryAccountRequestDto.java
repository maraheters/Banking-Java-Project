package example.banking.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryAccountRequestDto {
    private Long bankId;
    private Long holderId;
    private Long salaryProjectId;
    private BigDecimal salary;
}
