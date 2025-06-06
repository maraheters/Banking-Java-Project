package example.banking.user.controller;

import example.banking.user.dto.client.ClientResponseDto;
import example.banking.user.mapper.ClientMapper;
import example.banking.user.service.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/clients")
public class ClientsController {

    private final ClientsService service;

    @Autowired
    public ClientsController(ClientsService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<ClientResponseDto>> getAllUsers() {
        return ResponseEntity.ok(
            service.findAll().stream()
                    .map(ClientMapper::toResponseDto)
                    .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<ClientResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                ClientMapper.toResponseDto(service.findById(id)));
    }
}
