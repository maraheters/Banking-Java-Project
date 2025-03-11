package example.banking.account.service;

import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.types.AccountStatus;
import example.banking.account.types.AccountType;
import example.banking.exception.BadRequestException;
import example.banking.exception.ResourceNotFoundException;
import example.banking.security.BankingUserDetails;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.transaction.types.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountsService {

    private final AccountsRepository accountsRepository;
    private final TransactionsRepository transactionsRepository;

    @Autowired
    public AccountsService(
            AccountsRepository accountsRepository,
            TransactionsRepository transactionsRepository) {
        this.accountsRepository = accountsRepository;
        this.transactionsRepository = transactionsRepository;
    }

    public Long create(Long holderId, Long bankId) {
        var account = Account.create(holderId, bankId, AccountType.PERSONAL);

        return accountsRepository.create(account);
    }

    public Account getById(Long id) {
        return accountsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + id + "' not found."));
    }

    public List<Account> getAll() {
        return accountsRepository.findAll();
    }

    public BigDecimal withdraw(Long accountId, BigDecimal amount) {
        var account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found"));

        var transaction = Transaction.create(
                accountId, TransactionType.ACCOUNT, null, TransactionType.EXTERNAL, amount);

        account.withdraw(amount);
        accountsRepository.update(account);
        transactionsRepository.create(transaction);

        return amount;
    }

    public void topUp(Long accountId, BigDecimal amount) {
        var account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found"));

        var transaction = Transaction.create(
                null, TransactionType.EXTERNAL, accountId, TransactionType.ACCOUNT, amount);

        account.topUp(amount);
        accountsRepository.update(account);
        transactionsRepository.create(transaction);
    }

    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        var fromAccount = accountsRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + fromAccountId + "' not found"));

        var toAccount = accountsRepository.findById(toAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + toAccountId + "' not found"));

        var transaction = Transaction.create(
                fromAccountId, TransactionType.ACCOUNT, toAccountId, TransactionType.ACCOUNT, amount);

        fromAccount.withdraw(amount);
        toAccount.topUp(amount);

        accountsRepository.update(fromAccount);
        accountsRepository.update(toAccount);
        transactionsRepository.create(transaction);
    }

    public void activateAccount(Long id) {
        setStatus(id, AccountStatus.ACTIVE);
    }

    public void freezeAccount(Long id) {
        setStatus(id, AccountStatus.FROZEN);
    }

    private void setStatus(Long id, AccountStatus status) {
        var account = accountsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + id + "' not found"));

        account.setStatus(status);

        accountsRepository.update(account);
    }

    public List<Account> getAllByUser(BankingUserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("BASIC"))) {
            throw new BadRequestException("User is not a client");
        }

        return accountsRepository.findByUserId(userDetails.getId());
    }

    public boolean validateOwner(Long accountId, BankingUserDetails userDetails) {
        var account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found."));

        var clientId = userDetails.getClientId();
        return account.isOwner(clientId);
    }
}
