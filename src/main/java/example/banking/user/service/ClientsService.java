package example.banking.user.service;

import example.banking.exception.ResourceNotFoundException;
import example.banking.user.dto.client.RegisterClientRequestDto;
import example.banking.user.entity.Client;
import example.banking.user.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientsService {

    private final ClientsRepository repository;

    @Autowired
    public ClientsService(ClientsRepository repository) {
        this.repository = repository;
    }

    public Long register(RegisterClientRequestDto requestDto) {

        var client = Client.register(
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getPassportNumber(),
                requestDto.getIdentificationNumber(),
                requestDto.getEmail(),
                requestDto.getPasswordHash());

        return repository.create(client);
    }

    public List<Client> findAll() {
        return repository.findAll();
    }

    public Client findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id '" + id + "' not found"));
    }
}
