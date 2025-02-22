package example.banking.loan.strategies;

import java.math.BigDecimal;

public interface LoanPaymentStrategy {

    void pay(BigDecimal amount);
}
