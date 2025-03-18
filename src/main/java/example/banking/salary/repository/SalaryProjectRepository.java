package example.banking.salary.repository;

import example.banking.salary.model.SalaryProject;

import java.util.List;
import java.util.Optional;

public interface SalaryProjectRepository {

    Long create(SalaryProject project);

    void update(SalaryProject project);

    List<SalaryProject> findAll();

    List<SalaryProject> findAllByEnterpriseId(Long id);
    List<SalaryProject> findAllBySpecialistId(Long id);
    List<SalaryProject> findAllByAccountId(Long id);

    Optional<SalaryProject> findById(Long id);

    void delete(Long id);
}
