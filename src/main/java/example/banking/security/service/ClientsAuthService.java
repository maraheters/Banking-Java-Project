package example.banking.security.service;

import example.banking.exception.BadRequestException;
import example.banking.security.BankingUserDetails;
import example.banking.security.dto.UserAuthResponseDto;
import example.banking.security.repository.UserDetailsRepository;
import example.banking.user.dto.client.ClientRegisterRequestDto;
import example.banking.user.entity.Client;
import example.banking.user.repository.PendingClientsRepository;
import example.banking.user.roles.ClientRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsAuthService {

    private final PendingClientsRepository pendingClientsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final JwtService jwtService;

    @Value("${password.encoder.strength}")
    private int encoderStrength;

    public ClientsAuthService(
            PendingClientsRepository pendingClientsRepository,
            UserDetailsRepository userDetailsRepository,
            JwtService jwtService) {

        this.pendingClientsRepository = pendingClientsRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.jwtService = jwtService;
    }

    public UserAuthResponseDto requestRegister(ClientRegisterRequestDto requestDto) {
        var email = requestDto.getEmail();

        checkIfUserInTheSystem(email);

        var passwordHash = new BCryptPasswordEncoder(encoderStrength).encode(requestDto.getPassword());

        var client = Client.register(
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getPassportNumber(),
                requestDto.getIdentificationNumber(),
                email,
                passwordHash,
                List.of(ClientRole.BASIC));

        var id = pendingClientsRepository.create(client);
        var token = jwtService.generateToken(email);

        return new UserAuthResponseDto(
            id, email, token, List.of("PENDING")
        );
    }

    private void checkIfUserInTheSystem(String email) {
        Optional<BankingUserDetails> userOptional = userDetailsRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            throw new BadRequestException("User with email '" + email + "' already exists.");
        }

        Optional<Client> pendingClientOptional = pendingClientsRepository.findByEmail(email);

        if (pendingClientOptional.isPresent()) {
            throw new BadRequestException("User with email '" + email + "' is already pending for verification.");
        }
    }
}
