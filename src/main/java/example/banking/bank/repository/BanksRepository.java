package example.banking.bank.repository;

import example.banking.bank.model.Bank;

import java.util.List;
import java.util.Optional;

public interface BanksRepository {

    Long create(Bank bank);

    Optional<Bank> findById(Long id);

    List<Bank> findAll();
}
