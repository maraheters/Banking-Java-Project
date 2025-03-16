package example.banking.transaction.service;

import example.banking.account.entity.PersonalAccount;
import example.banking.account.repository.PersonalAccountsRepository;
import example.banking.contracts.FinancialEntity;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositsRepository;
import example.banking.exception.BadRequestException;
import example.banking.exception.ResourceNotFoundException;
import example.banking.loan.entity.Loan;
import example.banking.loan.repository.LoansRepository;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.transaction.types.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final DepositsRepository depositsRepository;
    private final PersonalAccountsRepository personalAccountsRepository;
    private final LoansRepository loansRepository;

    @Autowired
    public TransactionsService(
            TransactionsRepository transactionsRepository,
            DepositsRepository depositsRepository,
            PersonalAccountsRepository personalAccountsRepository,
            LoansRepository loansRepository) {
        this.transactionsRepository = transactionsRepository;
        this.depositsRepository = depositsRepository;
        this.personalAccountsRepository = personalAccountsRepository;
        this.loansRepository = loansRepository;
    }

    public List<Transaction> getAll() {
        return transactionsRepository.findAll();
    }

    public List<Transaction> getAllByUserId(Long id) {
        return transactionsRepository.findAllByUserId(id);
    }

    public List<Transaction> getAllByEntityId(Long id, TransactionType type) {
        return transactionsRepository.findAllByEntityId(id, type);
    }

    public Transaction getById(Long id) {
        return transactionsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id '" + id + "' not found"));
    }

    @Transactional
    public Long revertTransaction(Long id) {
        var originalTransaction = getById(id);

        var amount = originalTransaction.getAmount();
        var fromId = originalTransaction.getFromEntityId();
        var fromType = originalTransaction.getFromType();
        var toId = originalTransaction.getToEntityId();
        var toType = originalTransaction.getToType();

        if (toType.equals(TransactionType.EXTERNAL) || fromType.equals(TransactionType.EXTERNAL))
            throw new BadRequestException("Unable to undo transaction with EXTERNAL type.");

        if (originalTransaction.getRevertTransactionId() != null)
            throw new BadRequestException("Transaction has already been reversed");



        var fromEntity = findEntity(fromId, fromType);
        var toEntity = findEntity(toId, toType);

        fromEntity.topUp(amount);
        toEntity.withdraw(amount);

        var revertTransaction = Transaction.create(
                toId, toType, fromId, fromType, amount
        );

        var revertId = transactionsRepository.create(revertTransaction);
        originalTransaction.setRevertTransactionId(revertId);
        transactionsRepository.update(originalTransaction);

        updateEntity(fromEntity, fromType);
        updateEntity(toEntity, toType);

        return revertId;
    }

    private FinancialEntity findEntity(Long id, TransactionType type) {
        return switch (type) {
            case ACCOUNT -> personalAccountsRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            case LOAN -> loansRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
            case DEPOSIT -> depositsRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Deposit not found"));
            default -> throw new IllegalArgumentException("Unsupported entity type");
        };
    }

    private void updateEntity(FinancialEntity entity, TransactionType type) {
        switch (type) {
            case ACCOUNT -> personalAccountsRepository.update( (PersonalAccount) entity);
            case LOAN -> loansRepository.update( (Loan) entity);
            case DEPOSIT -> depositsRepository.update( (Deposit) entity);
            default -> throw new IllegalArgumentException("Unsupported entity type");
        }
    }
}
