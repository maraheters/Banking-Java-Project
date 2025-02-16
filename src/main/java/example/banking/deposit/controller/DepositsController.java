package example.banking.deposit.controller;

import example.banking.deposit.dto.DepositRequestDto;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.service.DepositsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deposits")
public class DepositsController {

    private final DepositsService service;

    public DepositsController(DepositsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Long> createDeposit(
            @RequestBody DepositRequestDto requestDto) {

        return ResponseEntity.ok(
            service.createDeposit(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<Deposit>> getAll() {
        return ResponseEntity.ok(
            service.getAll());
    }
}
