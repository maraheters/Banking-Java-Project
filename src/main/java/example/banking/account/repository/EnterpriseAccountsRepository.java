package example.banking.account.repository;

import example.banking.account.entity.EnterpriseAccount;

import java.util.List;
import java.util.Optional;

public interface EnterpriseAccountsRepository {

    Long create(EnterpriseAccount enterpriseAccount);

    void update(EnterpriseAccount enterpriseAccount);

    void batchUpdate(List<EnterpriseAccount> enterpriseAccounts);

    Optional<EnterpriseAccount> findBySalaryProjectId(Long salaryProjectId);

    List<EnterpriseAccount> findAll();

    List<EnterpriseAccount> findAllByEnterpriseId(Long enterpriseId);
    List<EnterpriseAccount> findAllBySpecialistId(Long specialistId);

    Optional<EnterpriseAccount> findById(Long id);
}
