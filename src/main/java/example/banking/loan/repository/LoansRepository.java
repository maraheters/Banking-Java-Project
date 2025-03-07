package example.banking.loan.repository;

import example.banking.loan.entity.Loan;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LoansRepository {

    @Transactional
    Long create(Loan loan);

    @Transactional
    void update(Loan loan);

    @Transactional
    void batchUpdate(List<Loan> loans);

    Optional<Loan> findById(Long id);

    List<Loan> findAll();

    List<Loan> findAllByUserId(Long id);
}
