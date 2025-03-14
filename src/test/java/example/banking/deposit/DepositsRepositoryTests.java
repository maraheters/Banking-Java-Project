package example.banking.deposit;

import example.banking.RepositoryTest;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositsRepository;
import example.banking.deposit.repository.DepositsRepositoryImpl;
import example.banking.deposit.types.DepositStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
@TestPropertySource(properties = """
    spring.flyway.locations=classpath:db/migration,\
                            classpath:db/seeders/client,\
                            classpath:db/seeders/bank,\
                            classpath:db/seeders/account
""")
public class DepositsRepositoryTests {

    private final DepositsRepository repository;
    private Deposit deposit1;
    private Deposit deposit2;

    @Autowired
    public DepositsRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new DepositsRepositoryImpl(template);
    }

    @BeforeEach
    public void setup() {
        deposit1 = Deposit.create(1L, BigDecimal.valueOf(1.5), 6, BigDecimal.valueOf(2000));
        deposit2 = Deposit.create(2L, BigDecimal.valueOf(3), 12, BigDecimal.valueOf(5000));
    }


    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenCreated_thenCorrect() {
        var id = repository.create(deposit1);
        assertNotNull(id);
    }

    @Test
    public void findById_whenCreatedAndFound_thenCorrect() {
        var id1 = repository.create(deposit1);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void create_whenSavedAndRetrieved_thenValuesCorrect() {
        var id = repository.create(deposit1);
        var retrieved = repository.findById(id).get();

        assertEquals(deposit1.toDto().getMinimum().compareTo(retrieved.toDto().getMinimum()), 0);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(deposit1);
        repository.create(deposit1);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    public void update_whenUpdated_thenCorrect() {
        var id = repository.create(deposit1);
        var deposit = repository.findById(id).get();

        var statusBefore = DepositStatus.COMPLETE;
        var interestRateBefore = BigDecimal.valueOf(7);

        var depositDto = deposit.toDto();
        depositDto.setStatus(statusBefore);
        depositDto.setInterestRate(interestRateBefore);
        repository.update(Deposit.fromDto(depositDto));

        var depositAfterUpdate = repository.findById(id).get();

        assertEquals(statusBefore, depositAfterUpdate.toDto().getStatus());
        assertEquals(interestRateBefore.doubleValue(), depositAfterUpdate.toDto().getInterestRate().doubleValue(), 0.0001);
    }

    @Test
    public void batchUpdate_whenUpdated_thenCorrect() {
        var id1 = repository.create(deposit1);
        var id2 = repository.create(deposit2);

        var depositDto1 = repository.findById(id1).get().toDto();
        var depositDto2 = repository.findById(id2).get().toDto();

        // Step 2: Prepare deposits for batch update
        depositDto1.setStatus(DepositStatus.COMPLETE);
        depositDto1.setInterestRate(BigDecimal.valueOf(8d));
        depositDto2.setStatus(DepositStatus.BLOCKED);
        depositDto2.setInterestRate(BigDecimal.valueOf(5d));

        List<Deposit> depositsToUpdate = List.of(Deposit.fromDto(depositDto1), Deposit.fromDto(depositDto2));

        // Step 3: Perform batch update
        repository.batchUpdate(depositsToUpdate);

        // Step 4: Fetch updated deposits
        var updatedDeposit1 = repository.findById(id1).get().toDto();
        var updatedDeposit2 = repository.findById(id2).get().toDto();

        // Step 5: Assert the updates
        assertEquals(DepositStatus.COMPLETE, updatedDeposit1.getStatus());
        assertEquals(BigDecimal.valueOf(8).doubleValue(), updatedDeposit1.getInterestRate().doubleValue(), 0.0001);

        assertEquals(DepositStatus.BLOCKED, updatedDeposit2.getStatus());
        assertEquals(BigDecimal.valueOf(5).doubleValue(), updatedDeposit2.getInterestRate().doubleValue(), 0.0001);
    }
}
