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

        return repository.create(account.toDto());
    }

    public Account getById(Long id) {
        var accountDto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + id + "' not found."));

        return Account.fromDto(accountDto);
    }

    public List<Account> getAll() {
        return repository.findAll().stream()
                .map(Account::fromDto)
                .toList();
    }

    public BigDecimal withdraw(Long accountId, BigDecimal amount) {
        var dto = repository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found"));

        var account = Account.fromDto(dto);
        account.withdraw(amount);
        repository.update(account.toDto());

        return amount;
    }

    public void topUp(Long accountId, BigDecimal amount) {
        var dto = repository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found"));

        var account = Account.fromDto(dto);
        account.topUp(amount);
        repository.update(account.toDto());
    }

    public void activateAccount(Long id) {
        setStatus(id, AccountStatus.ACTIVE);
    }

    public void freezeAccount(Long id) {
        setStatus(id, AccountStatus.FROZEN);
    }

    private void setStatus(Long id, AccountStatus status) {
        var dto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + id + "' not found"));

        var account =  Account.fromDto(dto);
        account.setStatus(status);

        repository.update(account.toDto());
    }
}
