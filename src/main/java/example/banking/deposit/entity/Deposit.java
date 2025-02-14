package example.banking.deposit.entity;

import example.banking.account.entity.Account;
import example.banking.deposit.types.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
    private Long id;
    private BigDecimal balance;
    private DepositStatus status;
    private LocalDateTime dateCreated;
    private Account account;
    private double interestRate;

    public static Deposit create(Account account, double interestRate) {
        Deposit deposit = new Deposit();
        deposit.account = account;
        deposit.interestRate = interestRate;
        deposit.status = DepositStatus.ACTIVE;
        deposit.balance = BigDecimal.ZERO;
        deposit.dateCreated = LocalDateTime.now();

        return deposit;
    }

    @Transactional
    public void transfer(BigDecimal amount, Deposit other) {
        this.withdraw(amount);
        other.topUp(amount);
    }

    @Transactional
    public void topUp(BigDecimal amount) {
        checkStatus();
        checkAmount(amount);

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        balance = balance.add(amount);
    }

    @Transactional
    public void withdraw(BigDecimal amount) {
        checkStatus();
        checkAmount(amount);

        if (balance.compareTo(amount) < 0 ) {
            throw new IllegalArgumentException("Balance insufficient");
        }

        balance = balance.subtract(amount);
    }

    private void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Amount must be greater than zero");
    }

    private void checkStatus() {
        if (status.equals(DepositStatus.FROZEN))
            throw new IllegalStateException("Deposit is Frozen");
        if (status.equals(DepositStatus.BLOCKED))
            throw new IllegalStateException("Deposit is Blocked");
    }

}
