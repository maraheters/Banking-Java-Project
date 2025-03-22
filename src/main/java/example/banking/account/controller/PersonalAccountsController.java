package example.banking.account.controller;

import example.banking.account.dto.PersonalAccountResponseDto;
import example.banking.account.mapper.AccountMapper;
import example.banking.account.service.GeneralAccountsService;
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
@RequestMapping("/personalAccounts")
public class PersonalAccountsController {

    private final PersonalAccountsService personalAccountsService;
    private final GeneralAccountsService generalAccountsService;

    @Autowired
    public PersonalAccountsController(
            PersonalAccountsService service,
            GeneralAccountsService generalAccountsService) {
        this.personalAccountsService = service;
        this.generalAccountsService = generalAccountsService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<Long> createAccount(
            @RequestParam("bankId") Long bankId,
            @AuthenticationPrincipal BankingUserDetails userDetails) {

        return ResponseEntity.ok(
                personalAccountsService.create(userDetails, bankId)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<PersonalAccountResponseDto>> getAll() {
        var dtos = personalAccountsService.getAll().stream().map(AccountMapper::toPersonalResponseDto).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
            hasAnyAuthority('MANAGER', 'ADMINISTRATOR') ||
            @personalAccountsService.validateOwner(#id, authentication.principal)
        """)
    public ResponseEntity<PersonalAccountResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                AccountMapper.toPersonalResponseDto(personalAccountsService.getById(id)));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<List<PersonalAccountResponseDto>> getAccountsByUser (
            @AuthenticationPrincipal BankingUserDetails userDetails) {
        var accounts = personalAccountsService.getAllByUser(userDetails);

        return ResponseEntity.ok(
                accounts.stream()
                        .map(AccountMapper::toPersonalResponseDto)
                        .toList()
        );
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<Void> activateAccount(@PathVariable("id") Long id) {
        generalAccountsService.activateAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/freeze")
    @PreAuthorize("""
            hasAnyAuthority('BASIC', 'MANAGER', 'ADMINISTRATOR') &&
            @personalAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> freezeAccount(@PathVariable("id") Long id) {
        generalAccountsService.freezeAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/top-up")
    @PreAuthorize("""
            hasAuthority('BASIC') &&
            @personalAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> topUp(
            @RequestParam("accountId") Long id,
            @RequestParam("amount") BigDecimal amount) {

        generalAccountsService.topUp(id, amount);
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
                generalAccountsService.withdraw(id, amount)
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

        generalAccountsService.transfer(fromAccountId, toAccountId, amount);

        return ResponseEntity.ok().build();
    }
}
