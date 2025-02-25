package example.banking.user.repository;

import example.banking.user.entity.Client;

import java.util.List;
import java.util.Optional;

public interface PendingClientsRepository {

    Long create(Client client);

    void delete(Long id);

    Optional<Client> findById(Long id);

    Optional<Client> findByEmail(String email);

    List<Client> findAll();
}
