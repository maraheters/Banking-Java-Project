package example.banking.account.controller;

import example.banking.account.dto.SalaryAccountRequestDto;
import example.banking.account.dto.SalaryAccountResponseDto;
import example.banking.account.mapper.AccountMapper;
import example.banking.account.service.GeneralAccountsService;
import example.banking.account.service.SalaryAccountsService;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/salaryAccounts")
public class SalaryAccountsController {

    private final SalaryAccountsService salaryAccountsService;
    private final GeneralAccountsService generalAccountsService;

    @Autowired
    public SalaryAccountsController(
            SalaryAccountsService salaryAccountsService,
            GeneralAccountsService generalAccountsService) {
        this.salaryAccountsService = salaryAccountsService;
        this.generalAccountsService = generalAccountsService;
    }

    @PostMapping
    @PreAuthorize("""
        hasAuthority('SPECIALIST') &&
        @salaryProjectService.verifySpecialist(#dto.salaryProjectId, authentication.principal)
    """)
    public ResponseEntity<Long> createAccount(
            @RequestBody SalaryAccountRequestDto dto,
            @AuthenticationPrincipal BankingUserDetails userDetails) {

        return ResponseEntity.ok(
                salaryAccountsService.create(userDetails, dto)
        );
    }

    @GetMapping("/salaryProject/{id}")
    @PreAuthorize("""
        hasAuthority('SPECIALIST') &&
        @salaryProjectService.verifySpecialist(#salaryProjectId, authentication.principal)
    """)
    public ResponseEntity<List<SalaryAccountResponseDto>> getAllBySalaryProjectId(
            @PathVariable("id") Long salaryProjectId) {

        return ResponseEntity.ok(
                salaryAccountsService.getAllBySalaryProjectId(salaryProjectId).stream()
                        .map(AccountMapper::toSalaryResponseDto)
                        .toList()
        );
    }
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<SalaryAccountResponseDto>> getAll() {
        var dtos = salaryAccountsService.getAll().stream()
                .map(AccountMapper::toSalaryResponseDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
            hasAnyAuthority('MANAGER', 'ADMINISTRATOR') ||
            @salaryAccountsService.validateOwner(#id, authentication.principal)
        """)
    public ResponseEntity<SalaryAccountResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                AccountMapper.toSalaryResponseDto(salaryAccountsService.getById(id)));
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<Void> activateAccount(@PathVariable("id") Long id) {
        generalAccountsService.activateAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/freeze")
    @PreAuthorize("""
            hasAnyAuthority('BASIC', 'MANAGER', 'ADMINISTRATOR', 'SPECIALIST') &&
            @salaryAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> freezeAccount(@PathVariable("id") Long id) {
        generalAccountsService.freezeAccount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/topUp")
    @PreAuthorize("""
            hasAuthority('SPECIALIST') &&
            @salaryAccountsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<Void> topUp(
            @RequestParam("accountId") Long id,
            @RequestParam("amount") BigDecimal amount) {

        generalAccountsService.topUp(id, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    @PreAuthorize("""
            hasAuthority('BASIC') &&
            @salaryAccountsService.validateOwner(#id, authentication.principal)""")
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
            @salaryAccountsService.validateOwner(#fromAccountId, authentication.principal)""")
    public ResponseEntity<Void> transfer(
            @RequestParam("fromAccountId") Long fromAccountId,
            @RequestParam("toAccountId") Long toAccountId,
            @RequestParam("amount") BigDecimal amount) {

        generalAccountsService.transfer(fromAccountId, toAccountId, amount);

        return ResponseEntity.ok().build();
    }
}
