package example.banking.user.controller;

import example.banking.contracts.PendingEntityStatus;
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
    public ResponseEntity<List<PendingClientResponseDto>> getAll(
            @RequestParam(value = "status", required = false) PendingEntityStatus status
    ) {
        var loansStream = service.getAll().stream()
                .map(ClientMapper::toPendingClientResponseDto);

        if (status != null) {
            loansStream = loansStream.filter(c -> c.getStatus().equals(status));
        }

        return ResponseEntity.ok(loansStream.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<PendingClientResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                ClientMapper.toPendingClientResponseDto(service.getById(id))
        );
    }

    @PostMapping("{id}/approve")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Long> approve(@PathVariable("id") Long id) {

        return ResponseEntity.ok(service.approve(id));
    }

    @PostMapping("{id}/reject")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Void> reject(@PathVariable("id") Long id) {

        service.reject(id);
        return ResponseEntity.ok().build();
    }
}
