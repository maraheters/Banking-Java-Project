package example.banking.security.service;

import example.banking.security.BankingUserDetails;
import example.banking.security.dto.UserAuthResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${password.encoder.strength}")
    private int encoderStrength;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserAuthResponseDto login(String email, String password) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        var token = jwtService.generateToken(email);

        BankingUserDetails client = (BankingUserDetails) authentication.getPrincipal();

        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::toString)
                .toList();

        return new UserAuthResponseDto(
                client.getId(), client.getUsername(), token, authorities
        );
    }
}
