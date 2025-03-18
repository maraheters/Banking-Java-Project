package example.banking.account.mapper;

import example.banking.account.dto.*;
import example.banking.account.entity.EnterpriseAccount;
import example.banking.account.entity.PersonalAccount;
import example.banking.account.entity.SalaryAccount;

public class AccountMapper {

    public static PersonalAccountResponseDto toPersonalResponseDto(PersonalAccount a) {
        return toPersonalResponseDto(a.toDto());
    }

    public static PersonalAccountResponseDto toPersonalResponseDto(PersonalAccountDto a) {
        return new PersonalAccountResponseDto(
                a.getId(),
                a.getIBAN(),
                a.getBalance(),
                a.getStatus(),
                a.getHolderId(),
                a.getCreatedAt()
        );
    }

    public static EnterpriseAccountResponseDto toEnterpriseResponseDto(EnterpriseAccount a) {
        return toEnterpriseResponseDto(a.toDto());
    }

    public static EnterpriseAccountResponseDto toEnterpriseResponseDto(EnterpriseAccountDto a) {
        return new EnterpriseAccountResponseDto(
                a.getId(),
                a.getEnterpriseId(),
                a.getSpecialistId(),
                a.getIBAN(),
                a.getBalance(),
                a.getStatus(),
                a.getBankId(),
                a.getCreatedAt()
        );
    }

    public static SalaryAccountResponseDto toSalaryResponseDto(SalaryAccount a) {
        return toSalaryResponseDto(a.toDto());
    }

    public static SalaryAccountResponseDto toSalaryResponseDto(SalaryAccountDto a) {
        return new SalaryAccountResponseDto(
                a.getId(),
                a.getBankId(),
                a.getHolderId(),
                a.getSalaryProjectId(),
                a.getIBAN(),
                a.getBalance(),
                a.getSalary(),
                a.getStatus(),
                a.getCreatedAt()
        );
    }
}
