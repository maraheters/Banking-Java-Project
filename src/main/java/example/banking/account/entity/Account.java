package example.banking.account.entity;

import example.banking.account.dto.AccountDto;
import example.banking.account.types.AccountStatus;
import example.banking.account.types.AccountType;
import example.banking.contracts.FinancialEntity;
import example.banking.exception.BadRequestException;
import example.banking.utils.IbanGenerator;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Account implements FinancialEntity {
    @Getter
    private Long id;
    private String IBAN;
    private BigDecimal balance;
    private AccountStatus status;
    private AccountType type;
    private Long holderId;
    private Long bankId;
    private LocalDateTime dateCreated;

    private Account(AccountDto dto) {
        id          = dto.getId();
        IBAN        = dto.getIBAN();
        balance     = dto.getBalance();
        status      = dto.getStatus();
        type        = dto.getType();
        holderId    = dto.getHolderId();
        bankId      = dto.getBankId();
        dateCreated = dto.getDateCreated();
    }

    public static Account create(Long clientId, Long bankId, AccountType type) {
        var account = new Account();
        account.IBAN = IbanGenerator.Generate("BY");
        account.balance = BigDecimal.ZERO;
        account.status = AccountStatus.ACTIVE;
        account.type = type;
        account.holderId = clientId;
        account.bankId = bankId;
        account.dateCreated = LocalDateTime.now();

        return account;
    }

    public static Account fromDto(AccountDto dto) {
        return new Account(dto);
    }

    public AccountDto toDto() {
        return new AccountDto(
                id, IBAN, balance, status, type, holderId, bankId, dateCreated);
    }

    @Transactional
    public void topUp(@Positive BigDecimal amount) throws BadRequestException {
        checkStatus(AccountStatus.ACTIVE);
        balance = balance.add(amount);
    }

    @Transactional
    public void withdraw(@Positive BigDecimal amount) throws BadRequestException {
        checkStatus(AccountStatus.ACTIVE);

        if (balance.compareTo(amount) < 0) {
            throw new BadRequestException("Balance insufficient");
        }

        balance = balance.subtract(amount);
    }

    public boolean isOwner(Long id) {
        return holderId.equals(id);
    }

    public void setStatus(AccountStatus status) throws BadRequestException {
        checkStatusNot(AccountStatus.BLOCKED);
        this.status = status;
    }

    private void checkStatus(AccountStatus status) {
        if (!this.status.equals(status))
            throw new BadRequestException("Account status must be " + status + ", actual status is: " + this.status);
    }

    private void checkStatusNot(AccountStatus status) {
        if (this.status.equals(status))
            throw new BadRequestException("Account status must not be: " + status);
    }
}
