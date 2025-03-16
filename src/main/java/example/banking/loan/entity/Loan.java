package example.banking.loan.entity;

import example.banking.contracts.FinancialEntity;
import example.banking.exception.BadRequestException;
import example.banking.loan.dto.LoanDto;
import example.banking.loan.types.LoanStatus;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Loan implements FinancialEntity {
    @Getter
    private Long id;
    @Getter
    private Long accountId;
    private BigDecimal principalAmount;
    private BigDecimal paidAmount;
    private BigDecimal interestRate; //in decimal form, eg 0.08 = 8%
    private Integer lengthInMonths;
    private LoanStatus status;
    private LocalDateTime createdAt;
    private LocalDate lastPayment;

    public static Loan create(
            Long accountId, BigDecimal amount, BigDecimal interestRate, Integer length) {
        var loan = new Loan();

        loan.accountId = accountId;
        loan.principalAmount = amount;
        loan.paidAmount = BigDecimal.ZERO;
        loan.interestRate = interestRate;
        loan.lengthInMonths = length;
        loan.status = LoanStatus.ACTIVE;
        loan.createdAt = LocalDateTime.now();
        loan.lastPayment = LocalDate.now();

        return loan;
    }

    public static Loan fromDto(LoanDto l) {
        return new Loan(
            l.getId(),
            l.getAccountId(),
            l.getPrincipalAmount(),
            l.getPaidAmount(),
            l.getInterestRate(),
            l.getLengthInMonths(),
            l.getStatus(),
            l.getCreatedAt(),
            l.getLastPayment());
    }

    public LoanDto toDto() {
        return new LoanDto(
            id, accountId, principalAmount, paidAmount, interestRate, lengthInMonths, status, createdAt, lastPayment);
    }

    @Override
    public void withdraw(@Positive BigDecimal amount) {
        if (!this.status.equals(LoanStatus.ACTIVE))
            throw new BadRequestException("Account status must be ACTIVE, actual status is: " + this.status);

        if (paidAmount.compareTo(amount) < 0)
            throw new BadRequestException("Balance insufficient.");

        paidAmount = paidAmount.subtract(amount);
    }

    @Override
    public void topUp(@Positive BigDecimal amount) {
        makePayment(amount);
    }

    public void makePayment(@Positive BigDecimal amount) {
        var remainingAmountToPay = principalAmount.subtract(paidAmount);

        if (remainingAmountToPay.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Payment amount is greater than remaining amount left to pay.");
        }

        paidAmount = paidAmount.add(amount);
        if (paidAmount.compareTo(principalAmount) >= 0) {
            status = LoanStatus.PAID_OFF;
        }
    }

    public boolean applyOverdueIfNecessary() {
        long monthsPassed = ChronoUnit.MONTHS.between(createdAt, LocalDateTime.now().plusDays(1));

        BigDecimal monthlyPay = principalAmount
                .divide(BigDecimal.valueOf(lengthInMonths), RoundingMode.HALF_UP)
                .multiply(interestRate.add(BigDecimal.ONE));

        BigDecimal expectedAmount = monthlyPay.multiply(BigDecimal.valueOf(monthsPassed));

        if (paidAmount.compareTo(expectedAmount) < 0) {
            status = LoanStatus.OVERDUE;
            return true;
        }

        return false;
    }

}
