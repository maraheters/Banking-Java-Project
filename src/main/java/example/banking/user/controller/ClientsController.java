package example.banking.user.controller;

import example.banking.user.dto.client.ClientResponseDto;
import example.banking.user.dto.client.RegisterClientRequestDto;
import example.banking.user.mapper.ClientMapper;
import example.banking.user.service.ClientsAuthService;
import example.banking.user.service.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/clients")
public class ClientsController {

    private final ClientsService service;
    private final ClientsAuthService authService;

    @Autowired
    public ClientsController(
            ClientsService service,
            ClientsAuthService authService) {
        this.service = service;
        this.authService = authService;
    }

    @PostMapping("/register-request")
    public ResponseEntity<Long> requestRegister(
            @RequestBody RegisterClientRequestDto requestDto) {

        return ResponseEntity.ok(
                authService.requestRegister(requestDto)
        );
    }


    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getAllUsers() {
        return ResponseEntity.ok(
            service.findAll().stream()
                    .map(ClientMapper::toResponseDto)
                    .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                ClientMapper.toResponseDto(service.findById(id)));
    }
}
