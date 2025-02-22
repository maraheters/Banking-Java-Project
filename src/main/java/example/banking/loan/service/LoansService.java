package example.banking.loan.service;

import example.banking.exception.ResourceNotFoundException;
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
}
