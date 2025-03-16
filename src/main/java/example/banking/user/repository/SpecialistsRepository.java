package example.banking.user.repository;

import example.banking.user.entity.Specialist;

import java.util.List;
import java.util.Optional;

public interface SpecialistsRepository {

    Long create(Specialist specialist);

    List<Specialist> findAll();

    Optional<Specialist> findById(Long id);
    Optional<Specialist> findByEmail(String email);
}
