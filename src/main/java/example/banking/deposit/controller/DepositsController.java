package example.banking.deposit.controller;

import example.banking.deposit.dto.DepositRequestDto;
import example.banking.deposit.dto.DepositResponseDto;
import example.banking.deposit.mapper.DepositMapper;
import example.banking.deposit.service.DepositsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Long> createDeposit(
            @RequestBody DepositRequestDto requestDto) {

        return ResponseEntity.ok(
            service.createDeposit(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<DepositResponseDto>> getAll() {
        var dtos = service.getAll().stream()
                .map(DepositMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
