package example.banking.account.mapper;

import example.banking.account.dto.EnterpriseAccountDto;
import example.banking.account.dto.EnterpriseAccountResponseDto;
import example.banking.account.dto.PersonalAccountResponseDto;
import example.banking.account.dto.PersonalAccountDto;
import example.banking.account.entity.EnterpriseAccount;
import example.banking.account.entity.PersonalAccount;

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
}
