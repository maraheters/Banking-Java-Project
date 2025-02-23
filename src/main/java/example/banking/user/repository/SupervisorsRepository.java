package example.banking.user.repository;

import example.banking.user.entity.Supervisor;

import java.util.List;
import java.util.Optional;

public interface SupervisorsRepository {

    Long create(Supervisor supervisor);

    List<Supervisor> findAll();

    Optional<Supervisor> findById(Long id);
}
