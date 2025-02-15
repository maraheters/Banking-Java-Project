package example.banking.deposit.repository;

import example.banking.deposit.entity.Deposit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DepositRepository {

    @Transactional
    Long create(Deposit deposit);

    Optional<Deposit> findById(long id);

    List<Deposit> findAll();
}
