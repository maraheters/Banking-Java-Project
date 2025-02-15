package example.banking.deposit;

import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositRepository;
import example.banking.deposit.repository.DepositRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Testcontainers
@ActiveProfiles("test-containers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepositRepositoryTests {

    private final DepositRepository repository;

    @Autowired
    public DepositRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new DepositRepositoryImpl(template);
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenCreated_thenCorrect() {
        var deposit = Deposit.create(null, 2);

        var id = repository.create(deposit);
        assertNotNull(id);
    }

    @Test
    public void findById_whenCreatedAndFound_thenCorrect() {
        var id1 = repository.create( Deposit.create(null, 2) );

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void create_whenSavedAndRetrieved_thenValuesCorrect() {
        var deposit = Deposit.create(null, 2);
        deposit.setBalance(BigDecimal.valueOf(24_000));

        var id = repository.create(deposit);

        var retrieved = repository.findById(id).get();

        assertEquals(deposit.getBalance().compareTo(retrieved.getBalance()), 0);

    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create( Deposit.create(null, 2) );
        repository.create( Deposit.create(null, 2) );

        var results = repository.findAll();
        assertEquals(2, results.size());
    }
}
