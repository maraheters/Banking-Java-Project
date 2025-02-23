package example.banking.user.repository;

import example.banking.user.entity.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ClientsRepository {

    @Transactional
    Long create(Client client);

    Optional<Client> findById(Long id);

    List<Client> findAll();
}
