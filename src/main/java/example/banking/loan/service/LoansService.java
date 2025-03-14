package example.banking.loan.service;

import example.banking.account.repository.AccountsRepository;
import example.banking.exception.BadRequestException;
import example.banking.exception.ResourceNotFoundException;
import example.banking.loan.entity.Loan;
import example.banking.loan.entity.PendingLoan;
import example.banking.loan.repository.LoansRepository;
import example.banking.loan.repository.PendingLoansRepository;
import example.banking.loan.types.LoanTerm;
import example.banking.security.BankingUserDetails;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.transaction.types.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LoansService {

    private final LoansRepository loansRepository;
    private final PendingLoansRepository pendingLoansRepository;
    private final AccountsRepository accountsRepository;
    private final TransactionsRepository transactionsRepository;

    @Autowired
    public LoansService(
            LoansRepository loansRepository,
            PendingLoansRepository pendingLoansRepository,
            AccountsRepository accountsRepository,
            TransactionsRepository transactionsRepository) {
        this.loansRepository = loansRepository;
        this.pendingLoansRepository = pendingLoansRepository;
        this.accountsRepository = accountsRepository;
        this.transactionsRepository = transactionsRepository;
    }

    public List<Loan> getAll() {
        return loansRepository.findAll();
    }

    public Loan getById(Long id) {
        return loansRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with id '" + id + "' not found."));
    }

    public List<LoanTerm> getAllTerms() {
        var terms = new ArrayList<LoanTerm>();
        Collections.addAll(terms, LoanTerm.values());

        return terms;
    }

    public List<PendingLoan> getAllPending() {
        return pendingLoansRepository.findAll();
    }

    public PendingLoan getByIdPending(Long id) {
        return pendingLoansRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Pending loan with id '" + id + "' not found.") );
    }

    public Long createLoanRequest(
            Long accountId, BigDecimal amount, LoanTerm term) {

        var loan = PendingLoan.create(accountId, amount, term.getInterestRate(), term.getMonths());

        return pendingLoansRepository.create(loan);
    }

    @Transactional
    public Long approveLoan(Long pendingLoanId) {
        var pendingLoan = pendingLoansRepository.findById(pendingLoanId)
                .orElseThrow( () -> new ResourceNotFoundException("Pending loan with id '" + pendingLoanId + "' not found.") );

        var dto = pendingLoan.toDto();

        var accountId = dto.getAccountId();
        var principalAmount = dto.getPrincipalAmount();
        var account = accountsRepository.findById(accountId)
                        .orElseThrow( () -> new RuntimeException("Account with id '" + accountId + "' not found."));

        var newLoan = Loan.create(accountId, principalAmount, dto.getInterestRate(), dto.getLengthInMonths());
        var loanId = loansRepository.create(newLoan);

        account.topUp(principalAmount);

        var transaction = Transaction.create(
                loanId, TransactionType.LOAN, accountId, TransactionType.ACCOUNT, principalAmount
        );

        pendingLoan.setApproved();
        pendingLoansRepository.update(pendingLoan);
        accountsRepository.update(account);
        transactionsRepository.create(transaction);

        return loanId;
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

    public boolean isOwner(Long loanId, BankingUserDetails userDetails) {
        var loan = loansRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan with id '" + loanId + "' not found."));
        var accountId = loan.getAccountId();
        var account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found."));

        var clientId = userDetails.getClientId();
        return account.isOwner(clientId);
    }
}
