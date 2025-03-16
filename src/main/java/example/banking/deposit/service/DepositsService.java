package example.banking.deposit.service;

import example.banking.account.repository.PersonalAccountsRepository;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositsRepository;
import example.banking.deposit.types.DepositStatus;
import example.banking.deposit.types.DepositTerm;
import example.banking.exception.BadRequestException;
import example.banking.exception.ResourceNotFoundException;
import example.banking.security.BankingUserDetails;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.transaction.types.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DepositsService {

    private final DepositsRepository depositsRepository;
    private final PersonalAccountsRepository personalAccountsRepository;
    private final TransactionsRepository transactionsRepository;

    @Autowired
    public DepositsService(
            DepositsRepository depositsRepository,
            PersonalAccountsRepository personalAccountsRepository,
            TransactionsRepository transactionsRepository) {
        this.depositsRepository = depositsRepository;
        this.personalAccountsRepository = personalAccountsRepository;
        this.transactionsRepository = transactionsRepository;
    }

    public List<Deposit> getAll() {
        return depositsRepository.findAll();
    }

    public Deposit getById(Long id) {

        return depositsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit with id '" + id + "' not found."));
    }

    public List<DepositTerm> getAllTerms() {
        var terms = new ArrayList<DepositTerm>();
        Collections.addAll(terms, DepositTerm.values());

        return terms;
    }

    @Transactional
    public Long create(Long accountId, DepositTerm term, BigDecimal amount) {
        var account = personalAccountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found."));

        var deposit = Deposit.create(
                accountId,
                term.getInterestRate(),
                term.getMonths(),
                amount
        );

        var depositId = depositsRepository.create(deposit);

        var transaction = Transaction.create(
                accountId, TransactionType.ACCOUNT, depositId, TransactionType.DEPOSIT, amount
        );

        account.withdraw(amount);
        personalAccountsRepository.update(account);
        transactionsRepository.create(transaction);

        return depositId;
    }

    @Transactional
    public void retrieveMoney(Long id) {
        var deposit = depositsRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Deposit with id '" + id + "' not found."));

        var accountId = deposit.getAccountId();

        var account = personalAccountsRepository.findById(accountId)
                .orElseThrow( () -> new RuntimeException("Account with id '" + id + "' not found."));

        var amount = deposit.retrieveMoney();
        account.topUp(amount);

        var transaction = Transaction.create(
                id, TransactionType.DEPOSIT, accountId, TransactionType.ACCOUNT, amount
        );

        personalAccountsRepository.update(account);
        depositsRepository.update(deposit);
        transactionsRepository.create(transaction);
    }

    public List<Deposit> getAllByClient(BankingUserDetails userDetails) {
        var authorities = userDetails.getAuthorities();
        if (!authorities.contains(new SimpleGrantedAuthority("BASIC"))) {
            throw new BadRequestException("User is not a client");
        }

        var clientId = userDetails.getId();

        return depositsRepository.findAllByClientId(clientId);
    }

    public void blockDeposit(Long id) {
        setStatus(id, DepositStatus.BLOCKED);
    }

    public void freezeDeposit(Long id) {
        setStatus(id, DepositStatus.FROZEN);
    }

    public void activateDeposit(Long id) {
        setStatus(id, DepositStatus.ACTIVE);
    }

    @Scheduled(fixedDelay = 3000)
    protected void addDueBonusesToDeposits() {
        var deposits = depositsRepository.findAll();

        // Add bonus to deposits that require it,
        // Then return them as a list for batch update
        List<Deposit> depositsToUpdate = deposits.stream()
                .filter(Deposit::addBonusIfRequired)
                .toList();

        depositsRepository.batchUpdate(depositsToUpdate);
    }

    private void setStatus(Long id, DepositStatus status) {
        var deposit = depositsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit with id '" + id + "' not found."));

        deposit.setStatus(status);

        depositsRepository.update(deposit);
    }

    public boolean validateOwner(Long depositId, BankingUserDetails userDetails) {
        var deposit = depositsRepository.findById(depositId)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit with id '" + depositId + "' not found."));
        var accountId = deposit.getAccountId();
        var account = personalAccountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found."));

        var clientId = userDetails.getId();
        return account.isOwner(clientId);
    }
}
