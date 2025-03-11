package example.banking.loan.service;

import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.exception.ResourceNotFoundException;
import example.banking.loan.entity.Loan;
import example.banking.loan.repository.LoansRepository;
import example.banking.loan.strategies.AccountLoanPaymentStrategy;
import example.banking.loan.strategies.ThinAirPaymentStrategy;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.transaction.types.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoansPaymentService {

    private final LoansRepository loansRepository;
    private final AccountsRepository accountsRepository;
    private final TransactionsRepository transactionsRepository;

    @Autowired
    public LoansPaymentService(
            LoansRepository loansRepository,
            AccountsRepository accountsRepository,
            TransactionsRepository transactionsRepository) {
        this.loansRepository = loansRepository;
        this.accountsRepository = accountsRepository;
        this.transactionsRepository = transactionsRepository;
    }

    public void payFromThinAir(BigDecimal amount, Long loanId) {
        Loan loan = loansRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with id '" + loanId + "' not found."));

        loan.makePayment(amount, new ThinAirPaymentStrategy());
        var transaction = Transaction.create(
                null, TransactionType.EXTERNAL, loanId, TransactionType.LOAN, amount
        );

        transactionsRepository.create(transaction);
        loansRepository.update(loan);
    }

    public void payFromAccount(BigDecimal amount, Long loanId) {

        Loan loan = loansRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with id '" + loanId + "' not found."));

        var accountId = loan.getAccountId();

        Account account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found."));

        var transaction = Transaction.create(
                accountId, TransactionType.ACCOUNT, loanId, TransactionType.LOAN, amount
        );

        transactionsRepository.create(transaction);

        loan.makePayment(amount, new AccountLoanPaymentStrategy(account));

        accountsRepository.update(account);
        loansRepository.update(loan);
    }

    @Scheduled(fixedDelay = 10000)
    private void checkOverdueLoans() {
        List<Loan> loans = loansRepository.findAll();

        List<Loan> overdueLoans = loans.stream()
                .filter(Loan::applyOverdueIfNecessary)
                .toList();

        loansRepository.batchUpdate(overdueLoans);
    }
}
