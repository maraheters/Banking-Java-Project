package example.banking.deposit.service;

import example.banking.deposit.dto.DepositDto;
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
        return repository.findAll().stream()
                .map(Deposit::fromDto)
                .toList();
    }

    public Deposit getById(Long id) {
        var depositDto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit with id '" + id + "' not found."));

        return Deposit.fromDto(depositDto);
    }

    public Long createDeposit(DepositRequestDto requestDto) {

        var deposit = Deposit.create(
                requestDto.getAccountId(),
                requestDto.getInterestRate(),
                requestDto.getLengthInMonths(),
                requestDto.getInitialBalance());

        return repository.create(deposit.toDto());
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
    private void addDueBonusesToDeposits() {
        var deposits = repository.findAll().stream().map(Deposit::fromDto).toList();

        // Add bonus to deposits that require it,
        // Then return them as a list for batch update
        List<DepositDto> depositsToUpdate = deposits.stream()
                .filter(Deposit::addBonusIfRequired)
                .map(Deposit::toDto)
                .toList();

        repository.batchUpdate(depositsToUpdate);
    }

    private void setStatus(Long id, DepositStatus status) {
        var depositDto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit with id '" + id + "' not found."));

        var depositEntity = Deposit.fromDto(depositDto);
        depositEntity.setStatus(status);

        repository.update(depositEntity.toDto());
    }

}
