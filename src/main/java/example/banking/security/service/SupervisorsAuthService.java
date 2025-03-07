package example.banking.security.service;

import example.banking.exception.BadRequestException;
import example.banking.security.dto.UserAuthResponseDto;
import example.banking.security.repository.UserDetailsRepository;
import example.banking.user.dto.supervisor.SupervisorRegisterRequestDto;
import example.banking.user.entity.Supervisor;
import example.banking.user.repository.SupervisorsRepository;
import example.banking.user.roles.SupervisorRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SupervisorsAuthService {

    private final SupervisorsRepository supervisorsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SupervisorsAuthService(
            SupervisorsRepository supervisorsRepository,
            UserDetailsRepository userDetailsRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.supervisorsRepository = supervisorsRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserAuthResponseDto register(SupervisorRegisterRequestDto requestDto) {
        var email = requestDto.getEmail();

        checkIfUserInTheSystem(email);

        var passwordHash = passwordEncoder.encode(requestDto.getPassword());

        var supervisor = Supervisor.register(
                requestDto.getName(),
                requestDto.getEmail(),
                passwordHash,
                requestDto.getRoles().stream()
                        .map(SupervisorRole::valueOf)
                        .toList(),
                requestDto.getCompanyId()
        );

        var id = supervisorsRepository.create(supervisor);
        var token = jwtService.generateToken(email);

        return new UserAuthResponseDto(
                id, email, token, requestDto.getRoles()
        );
    }

    private void checkIfUserInTheSystem(String email) {
        var supervisorOptional = userDetailsRepository.findByEmail(email);

        if (supervisorOptional.isPresent()) {
            throw new BadRequestException("User with email '" + email + "' already exists.");
        }
    }

}
