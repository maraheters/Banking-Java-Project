package example.banking.account.repository;

import example.banking.account.entity.SalaryAccount;

import java.util.List;
import java.util.Optional;

public interface SalaryAccountsRepository {

    Long create(SalaryAccount account);

    List<Long> batchCreate(List<SalaryAccount> accounts);

    void update(SalaryAccount account);

    Optional<SalaryAccount> findById(Long id);

    List<SalaryAccount> findAll();

    List<SalaryAccount> findAllBySalaryProjectId(Long id);

}
