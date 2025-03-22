package example.banking.enterprise.repository;

import example.banking.enterprise.model.Enterprise;

import java.util.List;
import java.util.Optional;

public interface EnterpriseRepository {

    Long create(Enterprise enterprise);

    void update(Enterprise enterprise);

    Optional<Enterprise> findById(Long id);

    Optional<Enterprise> findBySpecialistId(Long id);

    List<Enterprise> findAll();
}
