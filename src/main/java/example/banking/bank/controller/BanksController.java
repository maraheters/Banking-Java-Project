package example.banking.bank.controller;

import example.banking.bank.dto.BankDto;
import example.banking.bank.dto.BankRequestDto;
import example.banking.bank.model.Bank;
import example.banking.bank.service.BanksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banks")
public class BanksController {

    private final BanksService service;

    @Autowired
    public BanksController(BanksService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BankDto>> getAll() {
        return ResponseEntity.ok(
                service.getAll().stream()
                        .map(Bank::toDto)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                service.getById(id).toDto()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Long> create(BankRequestDto dto) {
        return ResponseEntity.ok(
                service.create(dto.getName(), dto.getBic(), dto.getAddress())
        );
    }
}
