package example.banking.account.entity;

import example.banking.account.dto.SalaryAccountDto;
import example.banking.account.types.AccountStatus;
import example.banking.utils.IbanGenerator;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SalaryAccount extends Account {

    private Long holderId;
    private Long salaryProjectId;
    @Getter
    private BigDecimal salary;

    public static SalaryAccount create(Long clientId, Long bankId, Long salaryProjectId, BigDecimal salary) {
        var account = new SalaryAccount();
        account.IBAN = IbanGenerator.Generate("BY");
        account.salaryProjectId = salaryProjectId;
        account.salary = salary;
        account.balance = BigDecimal.ZERO;
        account.status = AccountStatus.ACTIVE;
        account.holderId = clientId;
        account.bankId = bankId;
        account.createdAt = LocalDateTime.now();

        return account;
    }

    public static SalaryAccount fromDto(SalaryAccountDto dto) {
        var account = new SalaryAccount();

        account.id = dto.getId();
        account.holderId = dto.getHolderId();
        account.salaryProjectId = dto.getSalaryProjectId();
        account.salary = dto.getSalary();
        account.IBAN = dto.getIBAN();
        account.balance = dto.getBalance();
        account.status = dto.getStatus();
        account.bankId = dto.getBankId();
        account.createdAt = dto.getCreatedAt();

        return account;
    }

    public SalaryAccountDto toDto() {
        return new SalaryAccountDto(
                id, bankId, holderId, salaryProjectId, IBAN, balance, salary, status, createdAt
        );
    }

    public boolean isOwner(Long id) {
        return holderId.equals(id);
    }
}
