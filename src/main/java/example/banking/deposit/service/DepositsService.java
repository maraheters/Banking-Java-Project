package example.banking.deposit.service;

import example.banking.deposit.dto.DepositRequestDto;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositsService {

    private final DepositsRepository repository;

    @Autowired
    public DepositsService(DepositsRepository repository) {
        this.repository = repository;
    }

    public Long createDeposit(DepositRequestDto requestDto) {

        var deposit = Deposit.create(
                requestDto.getAccountId(),
                requestDto.getInterestRate(),
                requestDto.getLengthInMonths(),
                requestDto.getInitialBalance());

        return repository.create(deposit);
    }
}
