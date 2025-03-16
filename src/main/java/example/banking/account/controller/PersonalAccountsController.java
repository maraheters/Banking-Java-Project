package example.banking.account.controller;

import example.banking.account.dto.PersonalAccountResponseDto;
import example.banking.account.mapper.AccountMapper;
import example.banking.account.service.PersonalAccountsService;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts/personal")
public class PersonalAccountsController {

    private final PersonalAccountsService service;

    @Autowired
    public PersonalAccountsController(PersonalAccountsService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('BASIC', 'MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<Long> createAccount(
            @RequestParam("bankId") Long bankId,
            @AuthenticationPrincipal BankingUserDetails userDetails) {

        return ResponseEntity.ok(
                service.create(userDetails, bankId)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<PersonalAccountResponseDto>> getAll() {
        var dtos = service.getAll().stream().map(AccountMapper::toResponseDto).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
            hasAnyAuthority('MANAGER', 'ADMINISTRATOR') ||
            @personalAccountsService.validateOwner(#id, authentication.principal)
        """)
    public ResponseEntity<PersonalAccountResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                AccountMapper.toResponseDto(service.getById(id)));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<List<PersonalAccountResponseDto>> getAccountsByUser (
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
            @personalAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> freezeAccount(@PathVariable("id") Long id) {
        service.freezeAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/topUp")
    @PreAuthorize("""
            hasAuthority('BASIC') &&
            @personalAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> topUp(
            @RequestParam("accountId") Long id,
            @RequestParam("amount") BigDecimal amount) {

        service.topUp(id, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    @PreAuthorize("""
            hasAuthority('BASIC') &&
            @personalAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<BigDecimal> withdraw(
            @RequestParam("accountId") Long id,
            @RequestParam("amount") BigDecimal amount) {

        return ResponseEntity.ok(
                service.withdraw(id, amount)
        );
    }

    @PostMapping("/transfer")
    @PreAuthorize("""
            hasAuthority('BASIC') &&
            @personalAccountsService.validateOwner(#fromAccountId, authentication.principal)""")
    public ResponseEntity<Void> transfer(
            @RequestParam("fromAccountId") Long fromAccountId,
            @RequestParam("toAccountId") Long toAccountId,
            @RequestParam("amount") BigDecimal amount) {

        service.transfer(fromAccountId, toAccountId, amount);

        return ResponseEntity.ok().build();
    }
}
