package example.banking.loan.controller;

import example.banking.contracts.PendingEntityStatus;
import example.banking.loan.dto.LoanRequestDto;
import example.banking.loan.dto.LoanResponseDto;
import example.banking.loan.dto.LoanTermDto;
import example.banking.loan.dto.PendingLoanResponseDto;
import example.banking.loan.mapper.LoanMapper;
import example.banking.loan.service.LoansService;
import example.banking.loan.types.LoanTerm;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoansController {

    private final LoansService service;

    @Autowired
    public LoansController(LoansService service){
        this.service = service;
    }

    @GetMapping("/terms")
    public ResponseEntity<List<LoanTermDto>> getAllTerms() {
        return ResponseEntity.ok(
                service.getAllTerms().stream()
                        .map(LoanMapper::toLoanTermDto)
                        .toList()
        );
    }

    @PostMapping
    @PreAuthorize("""
        hasAuthority('BASIC') &&
        @personalAccountsService.validateOwner(#dto.accountId, authentication.principal)""")
    public ResponseEntity<Long> createLoan(
            @RequestBody LoanRequestDto dto){

        return ResponseEntity.ok(
                service.createLoanRequest(dto.getAccountId(), dto.getAmount(), LoanTerm.valueOf(dto.getTermName()))
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<LoanResponseDto>> getAll() {

        var loans = service.getAll().stream()
                .map(LoanMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    @PreAuthorize("""
        hasAnyAuthority('MANAGER', 'ADMINISTRATOR') ||
        @loansService.isOwner(#id, authentication.principal)""")
    public ResponseEntity<LoanResponseDto> getById(@PathVariable("id") Long id) {

        var loan = LoanMapper.toResponseDto(service.getById(id));

        return ResponseEntity.ok(loan);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<List<LoanResponseDto>> getAllForUser(
            @AuthenticationPrincipal BankingUserDetails userDetails) {

        return ResponseEntity.ok(
                service.getAllByUser(userDetails).stream()
                        .map(LoanMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<List<PendingLoanResponseDto>> getAllPending(
            @RequestParam(value = "status", required = false) PendingEntityStatus status
    ) {
        var loansStream = service.getAllPending().stream()
                .map(LoanMapper::toPendingLoanResponseDto);

        if (status != null) {
            loansStream = loansStream.filter(l -> l.getStatus().equals(status));
        }

        return ResponseEntity.ok(loansStream.toList());
    }

    @GetMapping("/pending/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<PendingLoanResponseDto> getByIdPending(@PathVariable("id") Long id) {

        var loan = LoanMapper.toPendingLoanResponseDto(service.getByIdPending(id));

        return ResponseEntity.ok(loan);
    }

    @PostMapping("/pending/{id}/approve")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<Long> approvePending(@PathVariable("id") Long id) {

        return ResponseEntity.ok(service.approveLoan(id));
    }

    @PostMapping("/pending/{id}/reject")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
    public ResponseEntity<Long> rejectPending(@PathVariable("id") Long id) {
        service.rejectLoan(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending/user")
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<List<PendingLoanResponseDto>> getAllPendingForUser(
            @AuthenticationPrincipal BankingUserDetails userDetails) {

        return ResponseEntity.ok(
                service.getAllPendingByUser(userDetails).stream()
                        .map(LoanMapper::toPendingLoanResponseDto)
                        .filter(l -> l.getStatus().equals(PendingEntityStatus.PENDING))
                        .toList()
        );
    }

 }
