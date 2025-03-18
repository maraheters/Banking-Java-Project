package example.banking.salary.dto;

import example.banking.salary.types.SalaryProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryProjectDto {
    private Long id;
    private Long accountId;
    private Long enterpriseId;
    private Long specialistId;
    private LocalDateTime createdAt;
    private BigDecimal totalSalary;
    private SalaryProjectStatus status;
}
