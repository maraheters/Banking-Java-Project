package example.banking.security.service;

import example.banking.exception.BadRequestException;
import example.banking.security.dto.UserAuthResponseDto;
import example.banking.security.repository.UserDetailsRepository;
import example.banking.user.dto.specialist.SpecialistRegisterRequestDto;
import example.banking.user.entity.Specialist;
import example.banking.user.repository.SpecialistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialistsAuthService {

    private final SpecialistsRepository specialistsRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SpecialistsAuthService(
            SpecialistsRepository specialistsRepository,
            UserDetailsRepository userDetailsRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {
        this.specialistsRepository = specialistsRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAuthResponseDto register(SpecialistRegisterRequestDto requestDto) {
        var email = requestDto.getEmail();

        checkIfUserInTheSystem(email);

        var passwordHash = passwordEncoder.encode(requestDto.getPassword());

        var specialist = Specialist.register(
                requestDto.getName(),
                requestDto.getEmail(),
                passwordHash,
                requestDto.getEnterpriseId()
        );

        var id = specialistsRepository.create(specialist);
        var token = jwtService.generateToken(email);

        return new UserAuthResponseDto(
                id, email, token, List.of("SPECIALIST")
        );
    }

    private void checkIfUserInTheSystem(String email) {
        var specialistOptional = userDetailsRepository.findByEmail(email);

        if (specialistOptional.isPresent()) {
            throw new BadRequestException("User with email '" + email + "' already exists.");
        }
    }

}
