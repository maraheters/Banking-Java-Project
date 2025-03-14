package example.banking.security.service;

import example.banking.exception.BadRequestException;
import example.banking.security.BankingUserDetails;
import example.banking.security.dto.UserAuthResponseDto;
import example.banking.security.repository.UserDetailsRepository;
import example.banking.user.dto.client.ClientRegisterRequestDto;
import example.banking.user.entity.PendingClient;
import example.banking.user.repository.PendingClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsAuthService {

    private final PendingClientsRepository pendingClientsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientsAuthService(
            PendingClientsRepository pendingClientsRepository,
            UserDetailsRepository userDetailsRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {

        this.pendingClientsRepository = pendingClientsRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAuthResponseDto requestRegister(ClientRegisterRequestDto requestDto) {
        var email = requestDto.getEmail();

        checkIfUserInTheSystem(email);

        var passwordHash = passwordEncoder.encode(requestDto.getPassword());

        var client = PendingClient.register(
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getPassportNumber(),
                requestDto.getIdentificationNumber(),
                email,
                passwordHash
        );

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

        Optional<PendingClient> pendingClientOptional = pendingClientsRepository.findByEmail(email);

        if (pendingClientOptional.isPresent()) {
            throw new BadRequestException("User with email '" + email + "' is already pending for verification.");
        }
    }
}
