package example.banking.account.controller;

import example.banking.account.dto.EnterpriseAccountResponseDto;
import example.banking.account.mapper.AccountMapper;
import example.banking.account.service.EnterpriseAccountsService;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enterpriseAccounts")
public class EnterpriseAccountsController {

    private final EnterpriseAccountsService enterpriseAccountsService;

    @Autowired
    public EnterpriseAccountsController(EnterpriseAccountsService enterpriseAccountsService) {
        this.enterpriseAccountsService = enterpriseAccountsService;
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

    @GetMapping("/salaryProjects/{id}")
    @PreAuthorize("""
            hasAnyAuthority('MANAGER', 'ADMINISTRATOR') ||
            @salaryProjectService.verifySpecialist(#id, authentication.principal)
        """)
    public ResponseEntity<EnterpriseAccountResponseDto> getBySalaryProjectId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                AccountMapper.toEnterpriseResponseDto(enterpriseAccountsService.getBySalaryProjectId(id)));
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
}
