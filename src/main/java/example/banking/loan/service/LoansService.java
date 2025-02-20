package example.banking.loan.service;

import example.banking.exception.ResourceNotFoundException;
import example.banking.strategies.ThinAirPaymentStrategy;
import example.banking.loan.entity.Loan;
import example.banking.loan.repository.LoansRepository;
import example.banking.loan.types.LoanTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoansService {

    private LoansRepository repository;

    @Autowired
    public LoansService(LoansRepository repository) {
        this.repository = repository;
    }

    public Long createWithPredefinedTerms(
        Long accountId, BigDecimal amount, String termName) {

        LoanTerm term = LoanTerm.valueOf(termName);

        Loan loan = Loan.create(accountId, amount, term.getInterestRate(), term.getMonths());

        return repository.create(loan);
    }

    public List<Loan> getAll() {
        return repository.findAll();
    }

    public Loan getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with id '" + id + "' not found."));
    }

//    public Long createWithCustomRate(
//            Long accountId, BigDecimal amount, BigDecimal interestRate, Integer length) {
//
//        Loan loan = Loan.create(accountId, amount, interestRate, length);
//    }
//
//    public void PayFromAccount(BigDecimal amount, Long loanId, Long accountId) {
//        Account account = abobabaoboa;
//        Loan loan = repository.findById(loanId);
//
//        loan.makePayment(amount, new AccountLoanPaymentStrategy(account));
//    }
//
    public void PayFromThinAir(BigDecimal amount, Long loanId) {
        Loan loan = repository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with id '" + loanId + "' not found."));

        loan.makePayment(amount, new ThinAirPaymentStrategy());
    }
}
