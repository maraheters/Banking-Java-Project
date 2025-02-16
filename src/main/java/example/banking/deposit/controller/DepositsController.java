package example.banking.deposit.controller;

import example.banking.deposit.dto.DepositRequestDto;
import example.banking.deposit.service.DepositsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
