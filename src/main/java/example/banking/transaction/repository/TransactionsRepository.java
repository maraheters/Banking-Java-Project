package example.banking.transaction.repository;

import example.banking.transaction.entity.Transaction;
import example.banking.transaction.types.TransactionType;

import java.util.List;
import java.util.Optional;

public interface TransactionsRepository {

    List<Transaction> findAll();

    List<Transaction> findAllByEntityId(Long entityId, TransactionType type);

    List<Transaction> findAllByUserId(Long userId);

    Optional<Transaction> findById(Long id);

    Long create(Transaction transaction);

    void delete(Long id);
}
