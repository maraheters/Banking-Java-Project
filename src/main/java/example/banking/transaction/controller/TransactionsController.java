package example.banking.transaction.controller;

import example.banking.transaction.dto.TransactionResponseDto;
import example.banking.transaction.mapper.TransactionMapper;
import example.banking.transaction.service.TransactionsService;
import example.banking.transaction.types.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@PreAuthorize("hasAuthority('MANAGER')")
public class TransactionsController {

    private final TransactionsService service;

    @Autowired
    public TransactionsController(TransactionsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAll() {
        return ResponseEntity.ok(
                service.getAll().stream()
                        .map(TransactionMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<List<TransactionResponseDto>> getAllByAccountId(@PathVariable("id") Long accountId) {
        return ResponseEntity.ok(
                service.getAllByEntityId(accountId, TransactionType.ACCOUNT).stream()
                        .map(TransactionMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/loans/{id}")
    public ResponseEntity<List<TransactionResponseDto>> getAllByLoanId(@PathVariable("id") Long loanId) {
        return ResponseEntity.ok(
                service.getAllByEntityId(loanId, TransactionType.LOAN).stream()
                        .map(TransactionMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/deposits/{id}")
    public ResponseEntity<List<TransactionResponseDto>> getAllByDepositId(@PathVariable("id") Long depositId) {
        return ResponseEntity.ok(
                service.getAllByEntityId(depositId, TransactionType.DEPOSIT).stream()
                        .map(TransactionMapper::toResponseDto)
                        .toList()
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<TransactionResponseDto>> getAllByUserId(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(
                service.getAllByUserId(userId).stream()
                        .map(TransactionMapper::toResponseDto)
                        .toList()
        );
    }
}
