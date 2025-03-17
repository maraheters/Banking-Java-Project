package example.banking.account.entity;

import example.banking.account.dto.EnterpriseAccountDto;
import example.banking.account.types.AccountStatus;
import example.banking.utils.IbanGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnterpriseAccount extends Account {

    private Long enterpriseId;
    private Long specialistId;

    public static EnterpriseAccount create(Long enterpriseId, Long specialistId, Long bankId) {
        var account = new EnterpriseAccount();
        account.enterpriseId = enterpriseId;
        account.specialistId = specialistId;
        account.IBAN = IbanGenerator.Generate("BY");
        account.bankId = bankId;
        account.status = AccountStatus.ACTIVE;
        account.balance = BigDecimal.ZERO;
        account.createdAt = LocalDateTime.now();

        return account;
    }

    public static EnterpriseAccount fromDto(EnterpriseAccountDto dto) {
        var account = new EnterpriseAccount();
        account.id = dto.getId();
        account.enterpriseId = dto.getEnterpriseId();
        account.specialistId = dto.getSpecialistId();
        account.IBAN = dto.getIBAN();
        account.bankId = dto.getBankId();
        account.status = dto.getStatus();
        account.balance = dto.getBalance();
        account.createdAt = dto.getCreatedAt();

        return account;
    }

    public EnterpriseAccountDto toDto() {
        var dto = new EnterpriseAccountDto();
        dto.setId(id);
        dto.setEnterpriseId(enterpriseId);
        dto.setSpecialistId(specialistId);
        dto.setIBAN(IBAN);
        dto.setBankId(bankId);
        dto.setStatus(status);
        dto.setBalance(balance);
        dto.setCreatedAt(createdAt);

        return dto;
    }

    public boolean isOwner(Long userId) {
        return specialistId.equals(userId);
    }
}
