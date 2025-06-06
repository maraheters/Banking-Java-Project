package example.banking.deposit.entity;

import example.banking.contracts.FinancialEntity;
import example.banking.deposit.dto.DepositDto;
import example.banking.deposit.types.DepositStatus;
import example.banking.exception.BadRequestException;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@ToString
public class Deposit implements FinancialEntity {
    @Getter
    private Long id;
    @Getter
    private Long accountId;
    private BigDecimal minimum;
    private BigDecimal bonus;
    private DepositStatus status;
    private LocalDateTime createdAt;
    private LocalDate lastBonusDate;
    private Integer numberOfBonusesYet;  // Used to track how many bonuses have been applied
    private Integer lengthInMonths;
    private BigDecimal interestRate;

    private Deposit(DepositDto dto) {
        id                  = dto.getId();
        minimum             = dto.getMinimum();
        bonus               = dto.getBonus();
        status              = dto.getStatus();
        createdAt           = dto.getCreatedAt();
        lastBonusDate       = dto.getLastBonusDate();
        numberOfBonusesYet  = dto.getNumberOfBonusesYet();
        lengthInMonths      = dto.getLengthInMonths();
        accountId           = dto.getAccountId();
        interestRate        = dto.getInterestRate();
    }

    public static Deposit create(
            Long accountId, BigDecimal interestRate, int lengthInMonths, BigDecimal initialBalance) {

        Deposit deposit = new Deposit();
        deposit.accountId = accountId;
        deposit.interestRate = interestRate;
        deposit.status = DepositStatus.ACTIVE;
        deposit.numberOfBonusesYet = 0;
        deposit.lengthInMonths = lengthInMonths;
        deposit.minimum = initialBalance;
        deposit.bonus = BigDecimal.ZERO;
        deposit.createdAt = LocalDateTime.now();
        deposit.lastBonusDate = LocalDate.now();

        return deposit;
    }

    public static Deposit fromDto(DepositDto dto) {
        return new Deposit(dto);
    }

    public DepositDto toDto() {
        return new DepositDto(
            id, minimum, bonus, status, createdAt, lastBonusDate, numberOfBonusesYet, lengthInMonths, accountId, interestRate);
    }

    public void setStatus(DepositStatus status) {
        checkStatusNot(DepositStatus.COMPLETE);
        checkStatusNot(DepositStatus.CLOSED);

        this.status = status;
    }

    @Transactional
    public void topUp(@Positive BigDecimal amount) {
        checkStatus(DepositStatus.ACTIVE);
        minimum = minimum.add(amount);
    }

    @Transactional
    public boolean addBonusIfRequired() {
        if (!isBonusDue()) {
            log.info(this.toString());
            return false;
        }

        bonus = bonus.add(calculateBonus());
        numberOfBonusesYet++;
        lastBonusDate = LocalDate.now();

        if (numberOfBonusesYet.compareTo(lengthInMonths) >= 0) {
            this.status = DepositStatus.COMPLETE;
        }

        log.info("Bonus added: {}", this);
        return true;
    }

    @Transactional
    public void withdraw(@Positive BigDecimal amount) {
        checkStatus(DepositStatus.ACTIVE);

        if (bonus.compareTo(amount) < 0 ) {
            throw new BadRequestException("Balance insufficient");
        }

        bonus = bonus.subtract(amount);
    }

    public BigDecimal retrieveMoney() {
        checkStatus(DepositStatus.COMPLETE);

        var total = minimum.add(bonus);

        minimum = BigDecimal.ZERO;
        bonus = BigDecimal.ZERO;

        status = DepositStatus.CLOSED;

        return total;
    }

    private boolean isBonusDue() {
        if (!status.equals(DepositStatus.ACTIVE))
            return false;

        var period = Period.between(lastBonusDate, LocalDate.now());
        return period.toTotalMonths() >= 1;
    }

    private void checkStatus(DepositStatus status) {
        if (!this.status.equals(status))
            throw new BadRequestException("Deposit status must be " + status + ", actual status is: " + this.status);
    }

    private void checkStatusNot(DepositStatus status) {
        if (this.status.equals(status))
            throw new BadRequestException("Deposit status must not be: " + status);
    }

    private BigDecimal calculateBonus() {
        return minimum
                .multiply(interestRate)
                .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP);
    }
}
