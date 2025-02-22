package example.banking.deposit.repository;

import example.banking.deposit.entity.Deposit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DepositsRepository {

    List<Deposit> findAll();

    Optional<Deposit> findById(Long id);

    @Transactional
    Long create(Deposit deposit);

    @Transactional
    void update(Deposit deposit);

    @Transactional
    void batchUpdate(List<Deposit> deposits);
}
