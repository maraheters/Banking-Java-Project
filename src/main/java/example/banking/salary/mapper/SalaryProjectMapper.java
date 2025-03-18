package example.banking.salary.mapper;

import example.banking.salary.dto.SalaryProjectDto;
import example.banking.salary.dto.SalaryProjectResponseDto;
import example.banking.salary.model.SalaryProject;

public class SalaryProjectMapper {

    public static SalaryProjectResponseDto toResponseDto(SalaryProjectDto dto) {
        return new SalaryProjectResponseDto(
                dto.getId(), dto.getEnterpriseId(), dto.getSpecialistId(), dto.getAccountId(), dto.getCreatedAt(), dto.getTotalSalary(), dto.getStatus()
        );
    }

    public static SalaryProjectResponseDto toResponseDto(SalaryProject project) {
        return toResponseDto(project.toDto());
    }
}
