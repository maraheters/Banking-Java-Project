package example.banking.account.entity;

import example.banking.account.dto.AccountDto;
import example.banking.account.types.AccountStatus;
import example.banking.account.types.AccountType;
import example.banking.utils.IbanGenerator;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {
    @Getter
    private Long id;
    private String IBAN;
    private BigDecimal balance;
    private AccountStatus status;
    private AccountType type;
    private Long holderId;
    private LocalDateTime dateCreated;
    private List<Long> depositIds;

    private Account(AccountDto dto) {
        id          = dto.getId();
        IBAN        = dto.getIBAN();
        balance     = dto.getBalance();
        status      = dto.getStatus();
        type        = dto.getType();
        holderId    = dto.getHolderId();
        dateCreated = dto.getDateCreated();
        depositIds  = dto.getDepositIds();
    }

    public static Account create(Long holderId, AccountType type) {
        var account = new Account();
        account.IBAN = IbanGenerator.Generate("BY");
        account.balance = BigDecimal.ZERO;
        account.status = AccountStatus.FROZEN;
        account.type = type;
        account.holderId = holderId;
        account.dateCreated = LocalDateTime.now();
        account.depositIds = new ArrayList<>();

        return account;
    }

    public static Account fromDto(AccountDto dto) {
        return new Account(dto);
    }

    public AccountDto toDto() {
        return new AccountDto(
                id, IBAN, balance, status, type, holderId, dateCreated, depositIds);
    }

    @Transactional
    public void topUp(@Positive BigDecimal amount) {
        checkStatus(AccountStatus.ACTIVE);
        balance = balance.add(amount);
    }

    @Transactional
    public BigDecimal withdraw(@Positive BigDecimal amount) {
        checkStatus(AccountStatus.ACTIVE);

        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Balance insufficient");
        }

        balance = balance.subtract(amount);
        return amount;
    }

    public void setStatus(AccountStatus status) {
        checkStatusNot(AccountStatus.BLOCKED);
        this.status = status;
    }

    private void checkStatus(AccountStatus status) {
        if (!this.status.equals(status))
            throw new IllegalStateException("Account status must be " + status + ", actual status is: " + this.status);
    }

    private void checkStatusNot(AccountStatus status) {
        if (this.status.equals(status))
            throw new IllegalStateException("Account status must not be: " + status);
    }
}
