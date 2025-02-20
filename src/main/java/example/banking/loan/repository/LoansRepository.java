package example.banking.loan.repository;

import example.banking.loan.entity.Loan;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LoansRepository {

    @Transactional
    Long create(Loan loan);

    @Transactional
    void update(Loan loan);

    Optional<Loan> findById(Long id);

    List<Loan> findAll();
}
