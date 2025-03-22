package example.banking.account.controller;

import example.banking.account.service.GeneralAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/generalAccounts")
public class GeneralAccountsController {

    private final GeneralAccountsService service;

    @Autowired
    public GeneralAccountsController(GeneralAccountsService service) {
        this.service = service;
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<Void> activateAccount(@PathVariable("id") Long id) {
        service.activateAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/freeze")
    @PreAuthorize("""
            hasAnyAuthority('SPECIALIST', 'MANAGER', 'ADMINISTRATOR') &&
            @generalAccountsService.validateOwner(#id, authentication.principal)
    """)
    public ResponseEntity<Void> freezeAccount(@PathVariable("id") Long id) {
        service.freezeAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/topUp")
    @PreAuthorize("""
            @generalAccountsService.validateOwner(#id, authentication.principal)
    """)
    public ResponseEntity<Void> topUp(
            @RequestParam("accountId") Long id,
            @RequestParam("amount") BigDecimal amount) {

        service.topUp(id, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    @PreAuthorize("""
            @generalAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<BigDecimal> withdraw(
            @RequestParam("accountId") Long id,
            @RequestParam("amount") BigDecimal amount) {

        return ResponseEntity.ok(
                service.withdraw(id, amount)
        );
    }

    @PostMapping("/transfer")
    @PreAuthorize("""
            @generalAccountsService.validateOwner(#fromAccountId, authentication.principal)""")
    public ResponseEntity<Void> transfer(
            @RequestParam("fromAccountId") Long fromAccountId,
            @RequestParam("toAccountId") Long toAccountId,
            @RequestParam("amount") BigDecimal amount) {

        service.transfer(fromAccountId, toAccountId, amount);

        return ResponseEntity.ok().build();
    }
}
