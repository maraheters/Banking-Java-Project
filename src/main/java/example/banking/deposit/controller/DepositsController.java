package example.banking.deposit.controller;

import example.banking.deposit.dto.DepositRequestDto;
import example.banking.deposit.dto.DepositResponseDto;
import example.banking.deposit.mapper.DepositMapper;
import example.banking.deposit.service.DepositsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deposits")
public class DepositsController {

    private final DepositsService service;

    @Autowired
    public DepositsController(
            DepositsService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<Long> createDeposit(
            @RequestBody DepositRequestDto requestDto) {

        return ResponseEntity.ok(
            service.createDeposit(requestDto));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MANAGER')")
    public ResponseEntity<List<DepositResponseDto>> getAll() {
        var dtos = service.getAll().stream()
                .map(DepositMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'MANAGER')")
    public ResponseEntity<DepositResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                DepositMapper.toResponseDto(service.getById(id)));
    }

    @PostMapping("/{id}/retrieve")
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<Void> retrieve(@PathVariable("id") Long id) {
        service.retrieveMoney(id);

        return ResponseEntity.ok().build();
    }
}
