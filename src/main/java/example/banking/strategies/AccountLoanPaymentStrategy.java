package example.banking.strategies;

import example.banking.account.entity.Account;

import java.math.BigDecimal;

public class AccountLoanPaymentStrategy implements LoanPaymentStrategy{

    private final Account account;

    public AccountLoanPaymentStrategy(Account account) {
        this.account = account;
    }

    @Override
    public void pay(BigDecimal amount) {
        account.withdraw(amount);
    }
}
