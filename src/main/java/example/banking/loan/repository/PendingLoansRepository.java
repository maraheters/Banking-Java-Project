package example.banking.loan.repository;

import example.banking.loan.entity.PendingLoan;

import java.util.List;
import java.util.Optional;

public interface PendingLoansRepository {

    List<PendingLoan> findAll();

    List<PendingLoan> findAllByUserId(Long id);

    Optional<PendingLoan> findById(Long id);

    void update(PendingLoan loan);

    Long create(PendingLoan loan);
}
