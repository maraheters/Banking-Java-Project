package example.banking.salary.dto;

import lombok.Data;

import java.util.List;

@Data
public class SalaryProjectRequestDto {
    private Long enterpriseAccountId;
    private List<SalaryAccountRequestDto> accountRequestDtos;
}
