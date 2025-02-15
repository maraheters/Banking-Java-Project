package example.banking.deposit.entity;

import example.banking.account.entity.Account;
import example.banking.deposit.types.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
    private Long id;
    private BigDecimal balance;
    private DepositStatus status;
    private LocalDate dateCreated;
    private Long accountId;
    private double interestRate;

    public static Deposit create(Long accountId, double interestRate) {
        Deposit deposit = new Deposit();
        deposit.accountId = accountId;
        deposit.interestRate = interestRate;
        deposit.status = DepositStatus.FROZEN;
        deposit.balance = BigDecimal.ZERO;
        deposit.dateCreated = LocalDateTime.now().toLocalDate();

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
