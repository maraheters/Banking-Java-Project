package example.banking.enterprise.controller;

import example.banking.enterprise.dto.EnterpriseDto;
import example.banking.enterprise.dto.EnterpriseRequestDto;
import example.banking.enterprise.model.Enterprise;
import example.banking.enterprise.service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    private final EnterpriseService service;

    @Autowired
    public EnterpriseController(EnterpriseService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<List<EnterpriseDto>> getAll() {
        return ResponseEntity.ok(
                service.getAll().stream()
                        .map(Enterprise::toDto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<EnterpriseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                service.getById(id).toDto()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Long> create(@RequestBody EnterpriseRequestDto dto) {
        return ResponseEntity.ok(
                service.create(dto.getType(), dto.getLegalName(), dto.getUnp(), dto.getBankId(), dto.getLegalAddress())
        );
    }
}
