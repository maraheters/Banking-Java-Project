package example.banking.account.controller;

import example.banking.account.dto.EnterpriseAccountResponseDto;
import example.banking.account.mapper.AccountMapper;
import example.banking.account.service.EnterpriseAccountsService;
import example.banking.account.service.GeneralAccountsService;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/enterprise-accounts/")
public class EnterpriseAccountsController {

    private final EnterpriseAccountsService enterpriseAccountsService;
    private final GeneralAccountsService generalAccountsService;

    @Autowired
    public EnterpriseAccountsController(
            EnterpriseAccountsService enterpriseAccountsService,
            GeneralAccountsService generalAccountsService) {
        this.enterpriseAccountsService = enterpriseAccountsService;
        this.generalAccountsService = generalAccountsService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<Long> createAccount(
            @RequestParam("bankId") Long bankId,
            @AuthenticationPrincipal BankingUserDetails userDetails) {

        return ResponseEntity.ok(
                enterpriseAccountsService.create(userDetails, bankId)
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<EnterpriseAccountResponseDto>> getAll() {
        var dtos = enterpriseAccountsService.getAll().stream()
                .map(AccountMapper::toEnterpriseResponseDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
            hasAnyAuthority('MANAGER', 'ADMINISTRATOR') ||
            @enterpriseAccountsService.validateOwner(#id, authentication.principal)
        """)
    public ResponseEntity<EnterpriseAccountResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                AccountMapper.toEnterpriseResponseDto(enterpriseAccountsService.getById(id)));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<List<EnterpriseAccountResponseDto>> getAccountsByUser (
            @AuthenticationPrincipal BankingUserDetails userDetails) {
        var accounts = enterpriseAccountsService.getAllByUser(userDetails);

        return ResponseEntity.ok(
                accounts.stream()
                        .map(AccountMapper::toEnterpriseResponseDto)
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
            hasAnyAuthority('SPECIALIST', 'MANAGER', 'ADMINISTRATOR') &&
            @enterpriseAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> freezeAccount(@PathVariable("id") Long id) {
        generalAccountsService.freezeAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/top-up")
    @PreAuthorize("""
            hasAuthority('SPECIALIST') &&
            @enterpriseAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> topUp(
            @RequestParam("accountId") Long id,
            @RequestParam("amount") BigDecimal amount) {

        generalAccountsService.topUp(id, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    @PreAuthorize("""
            hasAuthority('SPECIALIST') &&
            @enterpriseAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<BigDecimal> withdraw(
            @RequestParam("accountId") Long id,
            @RequestParam("amount") BigDecimal amount) {

        return ResponseEntity.ok(
                generalAccountsService.withdraw(id, amount)
        );
    }

    @PostMapping("/transfer")
    @PreAuthorize("""
            hasAuthority('SPECIALIST') &&
            @enterpriseAccountsService.validateOwner(#fromAccountId, authentication.principal)""")
    public ResponseEntity<Void> transfer(
            @RequestParam("fromAccountId") Long fromAccountId,
            @RequestParam("toAccountId") Long toAccountId,
            @RequestParam("amount") BigDecimal amount) {

        generalAccountsService.transfer(fromAccountId, toAccountId, amount);

        return ResponseEntity.ok().build();
    }
}
