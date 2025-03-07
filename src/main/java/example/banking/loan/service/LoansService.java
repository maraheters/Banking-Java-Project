package example.banking.loan.service;

import example.banking.exception.BadRequestException;
import example.banking.exception.ResourceNotFoundException;
import example.banking.loan.dto.PendingLoanResponseDto;
import example.banking.loan.entity.Loan;
import example.banking.loan.entity.PendingLoan;
import example.banking.loan.repository.LoansRepository;
import example.banking.loan.repository.PendingLoansRepository;
import example.banking.loan.types.LoanTerm;
import example.banking.security.BankingUserDetails;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoansService {

    private final LoansRepository loansRepository;
    private final PendingLoansRepository pendingLoansRepository;

    @Autowired
    public LoansService(
            LoansRepository loansRepository,
            PendingLoansRepository pendingLoansRepository) {
        this.loansRepository = loansRepository;
        this.pendingLoansRepository = pendingLoansRepository;
    }

    public List<Loan> getAll() {
        return loansRepository.findAll();
    }

    public Loan getById(Long id) {
        return loansRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with id '" + id + "' not found."));
    }

    public List<PendingLoan> getAllPending() {
        return pendingLoansRepository.findAll();
    }

    public PendingLoan getByIdPending(Long id) {
        return pendingLoansRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Pending loan with id '" + id + "' not found.") );
    }

    public Long createLoanRequest(
            Long accountId, BigDecimal amount, String termName) {

        var term = LoanTerm.valueOf(termName);

        var loan = PendingLoan.create(accountId, amount, term.getInterestRate(), term.getMonths());

        return pendingLoansRepository.create(loan);
    }

    @Transactional
    public Long approveLoan(Long pendingLoanId) {
        var pendingLoan = pendingLoansRepository.findById(pendingLoanId)
                .orElseThrow( () -> new ResourceNotFoundException("Pending loan with id '" + pendingLoanId + "' not found.") );

        var dto = pendingLoan.toDto();

        pendingLoan.setApproved();
        pendingLoansRepository.update(pendingLoan);

        var newLoan = Loan.create(dto.getAccountId(), dto.getPrincipalAmount(), dto.getInterestRate(), dto.getLengthInMonths());

        return loansRepository.create(newLoan);
    }

    @Transactional
    public void rejectLoan(Long pendingLoanId) {
        var pendingLoan = pendingLoansRepository.findById(pendingLoanId)
                .orElseThrow( () -> new ResourceNotFoundException("Pending loan with id '" + pendingLoanId + "' not found.") );

        pendingLoan.setRejected();
        pendingLoansRepository.update(pendingLoan);
    }

    public List<Loan> getAllByUser(BankingUserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("BASIC"))) {
            throw new BadRequestException("User is not a client");
        }

        return loansRepository.findAllByUserId(userDetails.getId());
    }

    public List<PendingLoan> getAllPendingByUser(BankingUserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("BASIC"))) {
            throw new BadRequestException("User is not a client");
        }

        return pendingLoansRepository.findAllByUserId(userDetails.getId());
    }


}
