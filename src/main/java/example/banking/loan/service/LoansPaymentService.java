package example.banking.loan.service;

import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.exception.ResourceNotFoundException;
import example.banking.loan.entity.Loan;
import example.banking.loan.repository.LoansRepository;
import example.banking.loan.strategies.AccountLoanPaymentStrategy;
import example.banking.loan.strategies.ThinAirPaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoansPaymentService {

    private final LoansRepository loansRepository;
    private final AccountsRepository accountsRepository;

    @Autowired
    public LoansPaymentService(
            LoansRepository loansRepository,
            AccountsRepository accountsRepository) {
        this.loansRepository = loansRepository;
        this.accountsRepository = accountsRepository;
    }

    public void payFromThinAir(BigDecimal amount, Long loanId) {
        Loan loan = loansRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with id '" + loanId + "' not found."));

        loan.makePayment(amount, new ThinAirPaymentStrategy());
    }

    public void payFromAccount(BigDecimal amount, Long loanId, Long accountId) {
        Account account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(""));
        Loan loan = loansRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(""));

        loan.makePayment(amount, new AccountLoanPaymentStrategy(account));
    }

    @Scheduled(fixedDelay = 10000)
    private void checkOverdueLoans() {
        List<Loan> loans = loansRepository.findAll();
        List<Loan> overdueLoans = new ArrayList<>();

        for (var loan : loans) {
            boolean isOverdue = loan.applyOverdueIfNecessary();
            if (isOverdue) {
                overdueLoans.add(loan);
            }
        }

        loansRepository.batchUpdate(overdueLoans);
    }
}
