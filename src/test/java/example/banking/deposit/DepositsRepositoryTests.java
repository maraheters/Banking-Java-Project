package example.banking.deposit;

import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.repository.AccountsRepositoryImpl;
import example.banking.account.types.AccountType;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositsRepository;
import example.banking.deposit.repository.DepositsRepositoryImpl;
import example.banking.deposit.types.DepositStatus;
import example.banking.user.entity.Client;
import example.banking.user.repository.ClientsRepository;
import example.banking.user.repository.ClientsRepositoryImpl;
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
    private final ClientsRepository clientsRepository;
    private final AccountsRepository accountsRepository;
    private Deposit deposit1;
    private Deposit deposit2;

    @Autowired
    public DepositsRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new DepositsRepositoryImpl(template);
        this.clientsRepository = new ClientsRepositoryImpl(template);
        this.accountsRepository = new AccountsRepositoryImpl(template);
    }

    @BeforeEach
    public void setup() {
        var user = Client.register(
                "Joe", "+375282828", "12345", "12345", "email@email.com", "password");
        Long userId = clientsRepository.create(user);
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
        double interestRateBefore = 7d;

        var depositDto = deposit.toDto();
        depositDto.setStatus(statusBefore);
        depositDto.setInterestRate(interestRateBefore);
        repository.update(Deposit.fromDto(depositDto));

        var depositAfterUpdate = repository.findById(id).get();

        assertEquals(statusBefore, depositAfterUpdate.toDto().getStatus());
        assertEquals(interestRateBefore, depositAfterUpdate.toDto().getInterestRate());
    }

    @Test
    public void batchUpdate_whenUpdated_thenCorrect() {
        var id1 = repository.create(deposit1);
        var id2 = repository.create(deposit2);

        var depositDto1 = repository.findById(id1).get().toDto();
        var depositDto2 = repository.findById(id2).get().toDto();

        // Step 2: Prepare deposits for batch update
        depositDto1.setStatus(DepositStatus.COMPLETE);
        depositDto1.setInterestRate(8d);
        depositDto2.setStatus(DepositStatus.BLOCKED);
        depositDto2.setInterestRate(5d);

        List<Deposit> depositsToUpdate = List.of(Deposit.fromDto(depositDto1), Deposit.fromDto(depositDto2));

        // Step 3: Perform batch update
        repository.batchUpdate(depositsToUpdate);

        // Step 4: Fetch updated deposits
        var updatedDeposit1 = repository.findById(id1).get().toDto();
        var updatedDeposit2 = repository.findById(id2).get().toDto();

        // Step 5: Assert the updates
        assertEquals(DepositStatus.COMPLETE, updatedDeposit1.getStatus());
        assertEquals(8d, updatedDeposit1.getInterestRate(), 0.001);

        assertEquals(DepositStatus.BLOCKED, updatedDeposit2.getStatus());
        assertEquals(5d, updatedDeposit2.getInterestRate(), 0.001);
    }
}
