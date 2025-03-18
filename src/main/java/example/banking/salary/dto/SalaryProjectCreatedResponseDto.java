package example.banking.salary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryProjectCreatedResponseDto {
    private Long projectId;
    private List<Long> accountIds;
}
