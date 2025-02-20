package example.banking.loan.entity;

import example.banking.strategies.LoanPaymentStrategy;
import example.banking.loan.dto.LoanDto;
import example.banking.loan.types.LoanStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Loan {
    @Getter
    private Long id;
    private Long accountId;
    private BigDecimal initialAmount;
    private BigDecimal paidAmount;
    private BigDecimal interestRate;
    private Integer lengthInMonths;
    private LoanStatus status;
    private LocalDate createdAt;
    private LocalDate lastPayment;

    public static Loan create(
            Long accountId, BigDecimal amount, BigDecimal interestRate, Integer length) {
        var loan = new Loan();

        loan.accountId = accountId;
        loan.initialAmount = amount;
        loan.paidAmount = BigDecimal.ZERO;
        loan.interestRate = interestRate;
        loan.lengthInMonths = length;
        loan.status = LoanStatus.ACTIVE;
        loan.createdAt = LocalDate.now();
        loan.lastPayment = LocalDate.now();

        return loan;
    }

    public static Loan fromDto(LoanDto l) {
        return new Loan(
            l.getId(),
            l.getAccountId(),
            l.getInitialAmount(),
            l.getPaidAmount(),
            l.getInterestRate(),
            l.getLengthInMonths(),
            l.getStatus(),
            l.getCreatedAt(),
            l.getLastPayment());
    }

    public LoanDto toDto() {
        return new LoanDto(
            id, accountId, initialAmount, paidAmount, interestRate, lengthInMonths, status, createdAt, lastPayment);
    }

    public void makePayment(BigDecimal amount, LoanPaymentStrategy strategy) {
        var remainingAmountToPay = initialAmount.subtract(paidAmount);

        if (remainingAmountToPay.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Payment amount is greater than remaining amount left to pay.");
        }

        strategy.pay(amount);

        paidAmount = paidAmount.add(amount);
        if (paidAmount.compareTo(initialAmount) >= 0) {
            status = LoanStatus.PAID_OFF;
        }
    }


}
