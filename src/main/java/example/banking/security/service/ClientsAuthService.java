package example.banking.security.service;

import example.banking.exception.BadRequestException;
import example.banking.exception.ResourceNotFoundException;
import example.banking.user.dto.client.RegisterClientRequestDto;
import example.banking.user.entity.Client;
import example.banking.user.repository.ClientsRepository;
import example.banking.user.roles.ClientRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientsAuthService {

    private final ClientsRepository repository;

    public ClientsAuthService(ClientsRepository repository) {
        this.repository = repository;
    }

    public Long register(RegisterClientRequestDto requestDto) {

        var client = Client.register(
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getPassportNumber(),
                requestDto.getIdentificationNumber(),
                requestDto.getEmail(),
                requestDto.getPasswordHash(),
                List.of(ClientRole.BASIC));

        return repository.create(client);
    }

    public void verify(Long id) {
        var client = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id '" + id + "' not found"));

        if (client.isVerified()) {
            throw new BadRequestException("User is already verified");
        }

        client.setVerified();
        repository.update(client);
    }
}
