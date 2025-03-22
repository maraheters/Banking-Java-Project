package example.banking.transaction;

import example.banking.RepositoryTest;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.transaction.repository.TransactionsRepositoryImpl;
import example.banking.transaction.types.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RepositoryTest
public class TransactionsRepositoryTests {

    private final TransactionsRepository repository;
    private final Transaction transaction1;
    private final Transaction transaction2;
    private final Long accountId = 1L;
    private final Long loanId = 2L;
    private final Long depositId = 10L;

    @Autowired
    public TransactionsRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new TransactionsRepositoryImpl(template);

        this.transaction1 = Transaction.create(
                accountId,
                TransactionType.ACCOUNT,
                loanId,
                TransactionType.LOAN,
                BigDecimal.TEN);

        this.transaction2 = Transaction.create(
                accountId,
                TransactionType.ACCOUNT,
                depositId,
                TransactionType.DEPOSIT,
                BigDecimal.TEN);
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenCreated_thenCorrect() {
        var id = repository.create(transaction1);
        assertNotNull(id);
    }

    @Test
    public void findByEntityId_whenFoundByAccount_thenCorrect() {
        repository.create(transaction1);
        repository.create(transaction2);

        var transactions = repository.findAllByEntityId(accountId, TransactionType.ACCOUNT);

        assertEquals(2, transactions.size());
    }

    @Test
    public void findByEntityId_whenFoundByLoan_thenCorrect() {
        repository.create(transaction1);
        repository.create(transaction2);

        var transactions = repository.findAllByEntityId(loanId, TransactionType.LOAN);

        assertEquals(1, transactions.size());
    }

    @Test
    public void findByEntityId_whenFoundByDeposit_thenCorrect() {
        repository.create(transaction1);
        repository.create(transaction2);

        var transactions = repository.findAllByEntityId(depositId, TransactionType.DEPOSIT);

        assertEquals(1, transactions.size());
    }

    @Test
    public void delete_whenDeleted_thenCorrect() {
        var id = repository.create(transaction1);
        repository.delete(id);

        var transactions = repository.findAll();

        assertEquals(0, transactions.size());
    }

    @Test
    public void update_whenRevertIdUpdated_thenCorrect() {
        var revertId = repository.create(transaction2);
        var id = repository.create(transaction1);
        var tr = repository.findById(id).get();

        tr.setRevertTransactionId(revertId);

        repository.update(tr);

        var updated = repository.findById(id).get();

        assertEquals(updated.getRevertTransactionId(), revertId);
    }

    @Test
    public void batchCreate_whenCreated_thenCorrect() {
        var ids = repository.batchCreate(List.of(transaction1, transaction2));

        assertEquals(2, ids.size());
    }
}
