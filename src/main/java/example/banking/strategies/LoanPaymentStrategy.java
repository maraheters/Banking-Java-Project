package example.banking.strategies;

import java.math.BigDecimal;

public interface LoanPaymentStrategy {

    void pay(BigDecimal amount);
}
