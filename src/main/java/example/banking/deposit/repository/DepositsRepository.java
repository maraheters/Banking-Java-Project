package example.banking.deposit.repository;

import example.banking.deposit.dto.DepositDto;
import example.banking.deposit.entity.Deposit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DepositsRepository {

    List<DepositDto> findAll();

    Optional<DepositDto> findById(long id);

    @Transactional
    Long create(DepositDto deposit);

    @Transactional
    void update(DepositDto deposit);

    @Transactional
    void batchUpdate(List<DepositDto> deposits);
}
