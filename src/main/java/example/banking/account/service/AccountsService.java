package example.banking.account.service;

import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.types.AccountStatus;
import example.banking.account.types.AccountType;
import example.banking.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountsService {

    private final AccountsRepository repository;

    @Autowired
    public AccountsService(AccountsRepository repository) {
        this.repository = repository;
    }

    public Long createAccount(Long holderId) {
        var account = Account.create(holderId, AccountType.PERSONAL);

        return repository.create(account);
    }

    public Account getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + id + "' not found."));
    }

    public List<Account> getAll() {
        return repository.findAll();
    }

    public BigDecimal withdraw(Long accountId, BigDecimal amount) {
        var account = repository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found"));

        account.withdraw(amount);
        repository.update(account);

        return amount;
    }

    public void topUp(Long accountId, BigDecimal amount) {
        var account = repository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found"));

        account.topUp(amount);
        repository.update(account);
    }

    public void activateAccount(Long id) {
        setStatus(id, AccountStatus.ACTIVE);
    }

    public void freezeAccount(Long id) {
        setStatus(id, AccountStatus.FROZEN);
    }

    private void setStatus(Long id, AccountStatus status) {
        var account = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + id + "' not found"));

        account.setStatus(status);

        repository.update(account);
    }
}
