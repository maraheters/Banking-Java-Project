package example.banking.user.controller;

import example.banking.user.dto.supervisor.SupervisorResponseDto;
import example.banking.user.mapper.SupervisorMapper;
import example.banking.user.service.SupervisorsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/supervisors")
public class SupervisorsController {

    private final SupervisorsService service;

    public SupervisorsController(SupervisorsService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<SupervisorResponseDto>> getAll() {

        return ResponseEntity.ok(
            service.getAll().stream()
                    .map(SupervisorMapper::toResponseDto)
                    .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<SupervisorResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                SupervisorMapper.toResponseDto(service.getById(id))
        );
    }
}
