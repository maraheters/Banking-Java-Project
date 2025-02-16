package example.banking.deposit.entity;

import example.banking.deposit.types.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
    private Long id;
    private BigDecimal minimum;
    private BigDecimal bonus;
    private DepositStatus status;
    private LocalDate dateCreated;
    private LocalDate lastBonusDate;
    private Integer numberOfBonusesYet;  // Used to track how many bonuses have been applied
    private Integer lengthInMonths;
    private Long accountId;
    private Double interestRate;

    public static Deposit create(
            Long accountId, double interestRate, int lengthInMonths, BigDecimal initialBalance) {

        Deposit deposit = new Deposit();
        deposit.accountId = accountId;
        deposit.interestRate = interestRate;
        deposit.status = DepositStatus.ACTIVE;
        deposit.lastBonusDate = null;
        deposit.numberOfBonusesYet = 0;
        deposit.lengthInMonths = lengthInMonths;
        deposit.minimum = initialBalance;
        deposit.dateCreated = LocalDate.now();

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

        minimum = minimum.add(amount);
    }

    @Transactional
    public void addBonus() {
        bonus = bonus.add(calculateBonus());
        numberOfBonusesYet++;
        lastBonusDate = LocalDate.now();
    }

    @Transactional
    public void withdraw(BigDecimal amount) {
        checkStatus();
        checkAmount(amount);

        if (bonus.compareTo(amount) < 0 ) {
            throw new IllegalArgumentException("Balance insufficient");
        }

        bonus = bonus.subtract(amount);
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

    private BigDecimal calculateBonus() {
        return minimum
                .multiply(BigDecimal.valueOf(interestRate))
                .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
    }

}
