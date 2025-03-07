package example.banking.deposit.service;

import example.banking.account.repository.AccountsRepository;
import example.banking.deposit.dto.DepositRequestDto;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositsRepository;
import example.banking.deposit.types.DepositStatus;
import example.banking.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepositsService {

    private final DepositsRepository depositsRepository;
    private final AccountsRepository accountsRepository;

    @Autowired
    public DepositsService(
            DepositsRepository depositsRepository,
            AccountsRepository accountsRepository) {
        this.depositsRepository = depositsRepository;
        this.accountsRepository = accountsRepository;
    }

    public List<Deposit> getAll() {
        return depositsRepository.findAll();
    }

    public Deposit getById(Long id) {

        return depositsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit with id '" + id + "' not found."));
    }

    public Long createDeposit(DepositRequestDto requestDto) {

        var deposit = Deposit.create(
                requestDto.getAccountId(),
                requestDto.getInterestRate(),
                requestDto.getLengthInMonths(),
                requestDto.getInitialBalance());

        return depositsRepository.create(deposit);
    }

    @Transactional
    public void retrieveMoney(Long id) {
        var deposit = depositsRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Deposit with id '" + id + "' not found."));

        var accountId = deposit.getAccountId();

        var account = accountsRepository.findById(accountId)
                .orElseThrow( () -> new RuntimeException("Account with id '" + id + "' not found."));

        var amount = deposit.retrieveMoney();
        account.topUp(amount);

        accountsRepository.update(account);
        depositsRepository.update(deposit);
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

}
