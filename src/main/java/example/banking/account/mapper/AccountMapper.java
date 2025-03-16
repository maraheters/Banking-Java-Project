package example.banking.account.mapper;

import example.banking.account.dto.PersonalAccountResponseDto;
import example.banking.account.dto.PersonalAccountDto;
import example.banking.account.entity.PersonalAccount;

public class AccountMapper {

    public static PersonalAccountResponseDto toResponseDto(PersonalAccount a) {
        return toResponseDto(a.toDto());
    }

    public static PersonalAccountResponseDto toResponseDto(PersonalAccountDto a) {
        return new PersonalAccountResponseDto(
                a.getId(),
                a.getIBAN(),
                a.getBalance(),
                a.getStatus(),
                a.getHolderId(),
                a.getCreatedAt()
        );
    }
}
