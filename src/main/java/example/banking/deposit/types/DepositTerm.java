package example.banking.deposit.types;

import lombok.Getter;

import java.math.BigDecimal;

public enum DepositTerm {
    THREE_MONTHS(3, BigDecimal.valueOf(0.05)),
    SIX_MONTHS(6, BigDecimal.valueOf(0.1)),
    TWELVE_MONTHS(12, BigDecimal.valueOf(0.15)),
    TWENTY_FOUR_MONTHS(24, BigDecimal.valueOf(0.2));

    @Getter
    private final Integer months;
    @Getter
    private final BigDecimal interestRate;

    DepositTerm(int months, BigDecimal interestRate) {
        this.months = months;
        this.interestRate = interestRate;
    }

}
