package example.banking.user.service;

import example.banking.exception.BadRequestException;
import example.banking.security.BankingUserDetails;
import example.banking.security.repository.UserDetailsRepository;
import example.banking.user.dto.client.RegisterClientRequestDto;
import example.banking.user.entity.Client;
import example.banking.user.repository.PendingClientsRepository;
import example.banking.user.roles.ClientRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsAuthService {

    private final PendingClientsRepository pendingClientsRepository;
    private final UserDetailsRepository userDetailsRepository;

    public ClientsAuthService(
            PendingClientsRepository pendingClientsRepository,
            UserDetailsRepository userDetailsRepository) {

        this.pendingClientsRepository = pendingClientsRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    public Long requestRegister(RegisterClientRequestDto requestDto) {
        var email = requestDto.getEmail();

        checkIfUserInTheSystem(email);

        var client = Client.register(
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getPassportNumber(),
                requestDto.getIdentificationNumber(),
                email,
                requestDto.getPasswordHash(),
                List.of(ClientRole.BASIC));

        return pendingClientsRepository.create(client);
    }

    private void checkIfUserInTheSystem(String email) {
        Optional<BankingUserDetails> userOptional = userDetailsRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            throw new BadRequestException("User with email '" + email + "' already exists.");
        }

        Optional<Client> clientOptional = pendingClientsRepository.findByEmail(email);

        if (clientOptional.isPresent()) {
            throw new BadRequestException("User with email '" + email + "' is already pending for verification.");
        }
    }
}
