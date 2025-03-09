package example.banking.transaction.service;

import example.banking.exception.ResourceNotFoundException;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.transaction.types.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsService {

    private final TransactionsRepository repository;

    @Autowired
    public TransactionsService(TransactionsRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> getAll() {
        return repository.findAll();
    }

    public List<Transaction> getAllByUserId(Long id) {
        return repository.findAllByUserId(id);
    }

    public List<Transaction> getAllByEntityId(Long id, TransactionType type) {
        return repository.findAllByEntityId(id, type);
    }

    public Transaction getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id '" + id + "' not found"));
    }
}
