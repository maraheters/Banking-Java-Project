package example.banking.user.controller;

import example.banking.user.dto.client.PendingClientResponseDto;
import example.banking.user.mapper.ClientMapper;
import example.banking.user.service.PendingClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/clients/pending")
public class PendingClientsController {

    private final PendingClientsService service;

    @Autowired
    public PendingClientsController(PendingClientsService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<PendingClientResponseDto>> getAll() {
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(ClientMapper::toPendingClientResponseDto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<PendingClientResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                ClientMapper.toPendingClientResponseDto(service.findById(id))
        );
    }

    @PostMapping("/verify")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public ResponseEntity<Long> verify(@RequestParam("id") Long id) {

        return ResponseEntity.ok(service.verify(id));
    }
}
