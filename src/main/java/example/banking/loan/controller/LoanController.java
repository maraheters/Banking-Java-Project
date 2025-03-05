package example.banking.loan.controller;

import example.banking.loan.dto.LoanResponseDto;
import example.banking.loan.mapper.LoanMapper;
import example.banking.loan.service.LoansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoansService service;

    @Autowired
    public LoanController(LoansService service){
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Long> createLoan(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam String name){

        return ResponseEntity.ok(
                service.createWithPredefinedTerms(accountId, amount, name));
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
 }
