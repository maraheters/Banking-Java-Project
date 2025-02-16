package example.banking.deposit.service;

import example.banking.deposit.dto.DepositRequestDto;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositsRepository;
import example.banking.deposit.types.DepositStatus;
import example.banking.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositsService {

    private final DepositsRepository repository;

    @Autowired
    public DepositsService(DepositsRepository repository) {
        this.repository = repository;
    }

    public List<Deposit> getAll() {
        return repository.findAll();
    }

    public Long createDeposit(DepositRequestDto requestDto) {

        var deposit = Deposit.create(
                requestDto.getAccountId(),
                requestDto.getInterestRate(),
                requestDto.getLengthInMonths(),
                requestDto.getInitialBalance());

        return repository.create(deposit);
    }

    public void BlockDeposit(Long id) {
        setStatus(id, DepositStatus.BLOCKED);
    }

    public void FreezeDeposit(Long id) {
        setStatus(id, DepositStatus.FROZEN);
    }

    public void ActivateDeposit(Long id) {
        setStatus(id, DepositStatus.ACTIVE);
    }

    @Scheduled(fixedDelay = 3000)
    private void addDueBonusesToDeposits() {
        var deposits = repository.findAll();

        // Add bonus to deposits that require it,
        // Then return them as a list for batch update
        List<Deposit> depositsToUpdate = deposits.stream()
                .filter(Deposit::addBonusIfRequired)
                .toList();

        repository.batchUpdate(depositsToUpdate);
    }

    private void setStatus(Long id, DepositStatus status) {
        var deposit = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit with id '" + id + "' not found."));

        deposit.setStatus(status);
        repository.update(deposit);
    }

}
