package example.banking.contracts;

import java.math.BigDecimal;

public interface FinancialEntity {
    void withdraw(BigDecimal amount);
    void topUp(BigDecimal amount);
    Long getId();
}