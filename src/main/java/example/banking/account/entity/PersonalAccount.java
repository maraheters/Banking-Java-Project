package example.banking.account.entity;

import example.banking.account.dto.PersonalAccountDto;
import example.banking.account.types.AccountStatus;
import example.banking.utils.IbanGenerator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonalAccount extends Account {

    private Long holderId;

    public static PersonalAccount create(Long clientId, Long bankId) {
        var account = new PersonalAccount();
        account.IBAN = IbanGenerator.Generate("BY");
        account.balance = BigDecimal.ZERO;
        account.status = AccountStatus.ACTIVE;
        account.holderId = clientId;
        account.bankId = bankId;
        account.createdAt = LocalDateTime.now();

        return account;
    }

    public static PersonalAccount fromDto(PersonalAccountDto dto) {
        var account = new PersonalAccount();

        account.id = dto.getId();
        account.holderId = dto.getHolderId();
        account.IBAN = dto.getIBAN();
        account.balance = dto.getBalance();
        account.status = dto.getStatus();
        account.bankId = dto.getBankId();
        account.createdAt = dto.getCreatedAt();

        return account;
    }

    public PersonalAccountDto toDto() {
        return new PersonalAccountDto(
                id, holderId, IBAN, balance, status, bankId, createdAt
        );
    }

    public boolean isOwner(Long id) {
        return holderId.equals(id);
    }
}
