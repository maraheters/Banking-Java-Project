package example.banking.security.controller;

import example.banking.security.dto.LoginRequestDto;
import example.banking.security.dto.UserAuthResponseDto;
import example.banking.security.service.AuthService;
import example.banking.security.service.SupervisorsAuthService;
import example.banking.user.dto.client.ClientRegisterRequestDto;
import example.banking.security.service.ClientsAuthService;
import example.banking.user.dto.supervisor.SupervisorRegisterRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClientsAuthService clientsAuthService;
    private final SupervisorsAuthService supervisorsAuthService;
    private final AuthService authService;

    @Autowired
    public AuthController(
            SupervisorsAuthService supervisorsAuthService,
            ClientsAuthService clientsAuthService,
            AuthService authService
    ) {
        this.supervisorsAuthService = supervisorsAuthService;
        this.clientsAuthService = clientsAuthService;
        this.authService = authService;
    }

    @PostMapping("/clients/register-request")
    public ResponseEntity<UserAuthResponseDto> registerClient(
            @RequestBody ClientRegisterRequestDto requestDto) {

        return ResponseEntity.ok(
                clientsAuthService.requestRegister(requestDto)
        );
    }

    @PostMapping("/supervisors/register")
    public ResponseEntity<UserAuthResponseDto> registerSupervisor(
            @RequestBody SupervisorRegisterRequestDto requestDto) {

        return ResponseEntity.ok(
                supervisorsAuthService.register(requestDto)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponseDto> login(
            @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(
                authService.login(request.getEmail(), request.getPassword())
        );
    }
}
