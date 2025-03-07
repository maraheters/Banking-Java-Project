package example.banking.loan.controller;

import example.banking.loan.service.LoansPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/loans/payments")
public class LoanPaymentsController {

    private final LoansPaymentService paymentService;

    @Autowired
    public LoanPaymentsController(LoansPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{id}/from-account")
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<Void> payFromAccount(
            @PathVariable("id") Long loanId,
            @RequestParam("amount") BigDecimal amount) {

        paymentService.payFromAccount(amount, loanId);

        return ResponseEntity.ok().build();
    }

    // Mimics some payment from other source
    @PostMapping("/{id}/from-other")
    @PreAuthorize("hasAuthority('BASIC')")
    public ResponseEntity<Void> payFromOther(
            @PathVariable("id") Long loanId,
            @RequestParam("amount") BigDecimal amount) {

        paymentService.payFromThinAir(amount, loanId);

        return ResponseEntity.ok().build();
    }
}
