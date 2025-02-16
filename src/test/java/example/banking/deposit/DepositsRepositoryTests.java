package example.banking.deposit;

import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.repository.AccountsRepositoryImpl;
import example.banking.account.types.AccountType;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositsRepository;
import example.banking.deposit.repository.DepositsRepositoryImpl;
import example.banking.deposit.types.DepositStatus;
import example.banking.user.entity.User;
import example.banking.user.repository.UsersRepository;
import example.banking.user.repository.UsersRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Testcontainers
@ActiveProfiles("test-containers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepositsRepositoryTests {

    private final DepositsRepository repository;
    private final UsersRepository usersRepository;
    private final AccountsRepository accountsRepository;
    private Deposit deposit1;
    private Deposit deposit2;

    @Autowired
    public DepositsRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new DepositsRepositoryImpl(template);
        this.usersRepository = new UsersRepositoryImpl(template);
        this.accountsRepository = new AccountsRepositoryImpl(template);
    }

    @BeforeEach
    public void setup() {
        var user = User.create(
                "Joe", "+375282828", "12345", "12345", "email@email.com");
        Long userId = usersRepository.create(user);
        var account = Account.create(userId, AccountType.PERSONAL);
        Long accountId = accountsRepository.create(account);

        deposit1 = Deposit.create(accountId, 1.5, 6, BigDecimal.valueOf(2000));
        deposit2 = Deposit.create(accountId, 3, 12, BigDecimal.valueOf(5000));
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

        assertEquals(deposit1.getMinimum().compareTo(retrieved.getMinimum()), 0);
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
        double interestRateBefore = 7d;

        deposit.setStatus(statusBefore);
        deposit.setInterestRate(interestRateBefore);
        repository.update(deposit);

        var depositAfterUpdate = repository.findById(id).get();

        assertEquals(statusBefore, depositAfterUpdate.getStatus());
        assertEquals(interestRateBefore, depositAfterUpdate.getInterestRate());
    }

    @Test
    public void batchUpdate_whenUpdated_thenCorrect() {
        var id1 = repository.create(deposit1);
        var id2 = repository.create(deposit2);

        deposit1 = repository.findById(id1).get();
        deposit2 = repository.findById(id2).get();

        // Step 2: Prepare deposits for batch update
        deposit1.setStatus(DepositStatus.COMPLETE);
        deposit1.setInterestRate(8d);
        deposit2.setStatus(DepositStatus.BLOCKED);
        deposit2.setInterestRate(5d);

        List<Deposit> depositsToUpdate = List.of(deposit1, deposit2);

        // Step 3: Perform batch update
        repository.batchUpdate(depositsToUpdate);

        // Step 4: Fetch updated deposits
        var updatedDeposit1 = repository.findById(id1).get();
        var updatedDeposit2 = repository.findById(id2).get();

        // Step 5: Assert the updates
        assertEquals(DepositStatus.COMPLETE, updatedDeposit1.getStatus());
        assertEquals(8d, updatedDeposit1.getInterestRate(), 0.001);

        assertEquals(DepositStatus.BLOCKED, updatedDeposit2.getStatus());
        assertEquals(5d, updatedDeposit2.getInterestRate(), 0.001);
    }
}
