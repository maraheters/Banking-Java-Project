package example.banking.user.repository;

import example.banking.user.entity.PendingClient;

import java.util.List;
import java.util.Optional;

public interface PendingClientsRepository {

    Long create(PendingClient client);

    void update(PendingClient client);

    void delete(Long id);

    Optional<PendingClient> findById(Long id);

    Optional<PendingClient> findByEmail(String email);

    List<PendingClient> findAll();
}
