package example.banking.user.service;

import example.banking.exception.ResourceNotFoundException;
import example.banking.user.entity.Client;
import example.banking.user.entity.PendingClient;
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

    public PendingClient getById(Long id) {
        return pendingClientsRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Pending client with id '" + id + "' not found"));
    }

    public List<PendingClient> getAll() {
        return pendingClientsRepository.findAll();
    }

    @Transactional
    public Long approve(Long id) {

        var pendingClient = pendingClientsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pending client with id '" + id + "' not found."));

        var dto = pendingClient.toDto();

        var client = Client.register(
                dto.getName(),
                dto.getPhoneNumber(),
                dto.getPassportNumber(),
                dto.getIdentificationNumber(),
                dto.getEmail(),
                dto.getPasswordHash(),
                List.of(ClientRole.BASIC)
        );

        var newId = clientsRepository.create(client);

        pendingClient.setApproved();
        pendingClientsRepository.update(pendingClient);

        return newId;
    }

    @Transactional
    public void reject(Long id) {
        var client = pendingClientsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pending client with id '" + id + "' not found."));

        client.setRejected();
        pendingClientsRepository.update(client);
    }
}
