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
public class SalaryProjectResponseDto {
    private Long id;
    private Long enterpriseId;
    private Long specialistId;
    private Long accountId;
    private LocalDateTime createdAt;
    private BigDecimal totalSalary;
    private SalaryProjectStatus status;
}
