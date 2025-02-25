package example.banking.user.service;

import example.banking.exception.ResourceNotFoundException;
import example.banking.user.entity.Client;
import example.banking.user.repository.ClientsRepository;
import example.banking.user.repository.PendingClientsRepository;
import example.banking.user.roles.ClientRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PendingClientsService {

    private final ClientsRepository clientsRepository;
    private final PendingClientsRepository pendingClientsRepository;

    @Autowired
    public PendingClientsService(
            ClientsRepository clientsRepository,
            PendingClientsRepository pendingClientsRepository) {
        this.clientsRepository = clientsRepository;
        this.pendingClientsRepository = pendingClientsRepository;
    }

    public Client findById(Long id) {
        return pendingClientsRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Pending client with id '" + id + "' not found"));
    }

    public List<Client> findAll() {
        return pendingClientsRepository.findAll();
    }

    @Transactional
    public Long verify(Long id) {

        var client = pendingClientsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pending client with id '" + id + "' not found."));

        client.addRoles(List.of(ClientRole.BASIC));

        var newId = clientsRepository.create(client);

        pendingClientsRepository.delete(id);

        return newId;
    }
}
