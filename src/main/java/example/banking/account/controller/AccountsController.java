package example.banking.account.controller;

import example.banking.account.dto.AccountResponseDto;
import example.banking.account.mapper.AccountMapper;
import example.banking.account.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountsService service;

    @Autowired
    public AccountsController(AccountsService service) {
        this.service = service;
    }

    @PostMapping
    public Long createAccount(@RequestParam("holder-id") Long holderId) {
        return service.createAccount(holderId);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAll() {
        var dtos = service.getAll().stream().map(AccountMapper::toResponseDto).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                AccountMapper.toResponseDto(service.getById(id)));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateAccount(@PathVariable("id") Long id) {
        service.activateAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/freeze")
    public ResponseEntity<Void> freezeAccount(@PathVariable("id") Long id) {
        service.freezeAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/top-up")
    public ResponseEntity<Void> topUp(
            @RequestParam("account-id") Long id,
            @RequestParam("amount") BigDecimal amount
    ) {
        service.topUp(id, amount);
        return ResponseEntity.ok().build();
    }
}
