package example.banking.salary.model;

import example.banking.exception.BadRequestException;
import example.banking.salary.dto.SalaryProjectDto;
import example.banking.salary.types.SalaryProjectStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SalaryProject {
    @Getter
    private Long id;
    @Getter
    private Long enterpriseId;
    private Long accountId;
    private Long specialistId;
    private LocalDateTime createdAt;
    private BigDecimal totalSalary;
    @Getter
    private SalaryProjectStatus status;

    public static SalaryProject register(Long enterpriseId, Long accountId) {
        var salaryProject = new SalaryProject();

        salaryProject.enterpriseId = enterpriseId;
        // This field does not have to be supplied for insertion in db
        // Because accountId is already specified, and account table stores specialist_id
        // However, it will be retrieved from salary_project_view during SELECT
        salaryProject.specialistId = null;
        salaryProject.accountId = accountId;
        salaryProject.createdAt = LocalDateTime.now();
        salaryProject.status = SalaryProjectStatus.PENDING;
        salaryProject.totalSalary = BigDecimal.ZERO;

        return salaryProject;
    }

    public static SalaryProject fromDto(SalaryProjectDto dto) {
        return new SalaryProject(
                dto.getId(), dto.getEnterpriseId(), dto.getAccountId(), dto.getSpecialistId(), dto.getCreatedAt(), dto.getTotalSalary(), dto.getStatus()
        );
    }

    public SalaryProjectDto toDto() {
        return new SalaryProjectDto(
                id, accountId, enterpriseId, specialistId, createdAt, totalSalary, status
        );
    }

    public void setActive() {
        checkStatus(SalaryProjectStatus.PENDING);
        status = SalaryProjectStatus.ACTIVE;
    }

    public void setClosed() {
        checkStatus(SalaryProjectStatus.ACTIVE);
        status = SalaryProjectStatus.CLOSED;
    }

    private void checkStatus(SalaryProjectStatus status) {
        if (!this.status.equals(status)) {
            throw new BadRequestException("Current status be '" + status + "'.");
        }
    }

    public boolean verifySpecialist(Long specialistId) {
        return this.specialistId.equals(specialistId);
    }
}
