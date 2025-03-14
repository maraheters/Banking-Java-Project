package example.banking.deposit.controller;

import example.banking.deposit.dto.DepositRequestDto;
import example.banking.deposit.dto.DepositResponseDto;
import example.banking.deposit.dto.DepositTermDto;
import example.banking.deposit.mapper.DepositMapper;
import example.banking.deposit.service.DepositsService;
import example.banking.deposit.types.DepositStatus;
import example.banking.deposit.types.DepositTerm;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/terms")
    public ResponseEntity<List<DepositTermDto>> getAllTerms() {

        var terms = service.getAllTerms();

        return ResponseEntity.ok(
                terms.stream()
                        .map(DepositMapper::toDepositTermDto)
                        .toList()
        );
    }

    @PostMapping
    @PreAuthorize("""
        hasAuthority('BASIC') &&
        @accountsService.validateOwner(#dto.accountId, authentication.principal)""")
    public ResponseEntity<Long> createDeposit(
            @RequestBody DepositRequestDto dto) {

        return ResponseEntity.ok(
            service.create(dto.getAccountId(), DepositTerm.valueOf(dto.getTermName()), dto.getAmount())
        );
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
    @PreAuthorize("""
            hasAnyAuthority('ADMINISTRATOR', 'MANAGER') ||
            @depositsService.validateOwner(#id, authentication.principal)""")
    public ResponseEntity<DepositResponseDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                DepositMapper.toResponseDto(service.getById(id)));
    }

    @PostMapping("/{id}/retrieve")
    @PreAuthorize("hasAuthority('BASIC') && @depositsService.validateOwner(#id, authentication.principal)")
    public ResponseEntity<Void> retrieve(@PathVariable("id") Long id) {
        service.retrieveMoney(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<List<DepositResponseDto>> getAllByUser(
            @AuthenticationPrincipal BankingUserDetails userDetails) {

        return ResponseEntity.ok(
                service.getAllByClient(userDetails).stream()
                        .map(DepositMapper::toResponseDto)
                        .filter(d -> d.getStatus().equals(DepositStatus.ACTIVE))
                        .toList()
        );
    }
}
