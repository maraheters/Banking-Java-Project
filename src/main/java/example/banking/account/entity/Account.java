package example.banking.account.entity;

import example.banking.account.types.AccountStatus;
import example.banking.contracts.FinancialEntity;
import example.banking.exception.BadRequestException;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Account implements FinancialEntity {
    @Getter
    protected Long id;
    protected String IBAN;
    protected BigDecimal balance;
    protected AccountStatus status;
    protected Long bankId;
    protected LocalDateTime createdAt;


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

    public void setStatus(AccountStatus status) throws BadRequestException {
        checkStatusNot(AccountStatus.BLOCKED);
        this.status = status;
    }

    protected void checkStatus(AccountStatus status) {
        if (!this.status.equals(status))
            throw new BadRequestException("Account status must be " + status + ", actual status is: " + this.status);
    }

    protected void checkStatusNot(AccountStatus status) {
        if (this.status.equals(status))
            throw new BadRequestException("Account status must not be: " + status);
    }
}
