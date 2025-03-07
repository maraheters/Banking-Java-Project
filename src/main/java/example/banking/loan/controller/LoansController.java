package example.banking.loan.controller;

import example.banking.loan.dto.LoanResponseDto;
import example.banking.loan.dto.PendingLoanResponseDto;
import example.banking.loan.mapper.LoanMapper;
import example.banking.loan.service.LoansService;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoansController {

    private final LoansService service;

    @Autowired
    public LoansController(LoansService service){
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<Long> createLoan(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam String name){

        return ResponseEntity.ok(
                service.createLoanRequest(accountId, amount, name));
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
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMINISTRATOR')")
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
    public ResponseEntity<List<PendingLoanResponseDto>> getAllPending() {

        var loans = service.getAllPending().stream()
                .map(LoanMapper::toPendingLoanResponseDto)
                .toList();

        return ResponseEntity.ok(loans);
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
                        .toList()
        );
    }

 }
