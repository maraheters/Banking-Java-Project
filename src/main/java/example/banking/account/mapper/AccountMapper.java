package example.banking.account.mapper;

import example.banking.account.dto.AccountDto;
import example.banking.account.dto.AccountResponseDto;
import example.banking.account.entity.Account;

public class AccountMapper {

    public static AccountResponseDto toResponseDto(Account a) {
        return toResponseDto(a.toDto());
    }

    public static AccountResponseDto toResponseDto(AccountDto a) {
        return new AccountResponseDto(
                a.getId(),
                a.getIBAN(),
                a.getBalance(),
                a.getStatus(), a.getType(),
                a.getHolderId(),
                a.getCreatedAt()
        );
    }
}
