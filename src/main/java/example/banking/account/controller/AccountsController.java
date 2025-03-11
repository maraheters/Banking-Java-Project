package example.banking.account.controller;

import example.banking.account.dto.AccountResponseDto;
import example.banking.account.mapper.AccountMapper;
import example.banking.account.service.AccountsService;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasAnyAuthority('BASIC', 'MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<Long> createAccount(
            @RequestParam("holder-id") Long holderId,
            @RequestParam("bank-id") Long bankId) {

        return ResponseEntity.ok(
                service.create(holderId, bankId)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<AccountResponseDto>> getAll() {
        var dtos = service.getAll().stream().map(AccountMapper::toResponseDto).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<AccountResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                AccountMapper.toResponseDto(service.getById(id)));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByUser (
            @AuthenticationPrincipal BankingUserDetails userDetails) {
        var accounts = service.getAllByUser(userDetails);

        return ResponseEntity.ok(
                accounts.stream()
                        .map(AccountMapper::toResponseDto)
                        .toList()
        );
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<Void> activateAccount(@PathVariable("id") Long id) {
        service.activateAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/freeze")
    @PreAuthorize("""
            hasAnyAuthority('BASIC', 'MANAGER', 'ADMINISTRATOR') &&
            @accountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> freezeAccount(@PathVariable("id") Long id) {
        service.freezeAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/top-up")
    @PreAuthorize("""
            hasAuthority('BASIC') &&
            @accountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> topUp(
            @RequestParam("account-id") Long id,
            @RequestParam("amount") BigDecimal amount) {

        service.topUp(id, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    @PreAuthorize("""
            hasAuthority('BASIC') &&
            @accountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<BigDecimal> withdraw(
            @RequestParam("account-id") Long id,
            @RequestParam("amount") BigDecimal amount) {

        return ResponseEntity.ok(
                service.withdraw(id, amount)
        );
    }

    @PostMapping("/transfer")
    @PreAuthorize("""
            hasAuthority('BASIC') &&
            @accountsService.validateOwner(#fromAccountId, authentication.principal)""")
    public ResponseEntity<Void> transfer(
            @RequestParam("from-account-id") Long fromAccountId,
            @RequestParam("to-account-id") Long toAccountId,
            @RequestParam("amount") BigDecimal amount) {

        service.transfer(fromAccountId, toAccountId, amount);

        return ResponseEntity.ok().build();
    }
}
