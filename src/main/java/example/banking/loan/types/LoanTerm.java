package example.banking.loan.types;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum LoanTerm {
    THREE_MONTHS(3, BigDecimal.valueOf(0.05)),
    SIX_MONTHS(6, BigDecimal.valueOf(0.08)),
    TWELVE_MONTHS(12, BigDecimal.valueOf(0.12)),
    TWENTY_FOUR_MONTHS(24, BigDecimal.valueOf(0.18));

    private final Integer months;
    private final BigDecimal interestRate;

    LoanTerm(int months, BigDecimal interestRate) {
        this.months = months;
        this.interestRate = interestRate;
    }
}
