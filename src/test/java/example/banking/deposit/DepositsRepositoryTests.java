package example.banking.deposit;

import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.repository.AccountsRepositoryImpl;
import example.banking.account.types.AccountType;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositsRepository;
import example.banking.deposit.repository.DepositsRepositoryImpl;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Testcontainers
@ActiveProfiles("test-containers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepositsRepositoryTests {

    private final DepositsRepository repository;
    private final UsersRepository usersRepository;
    private final AccountsRepository accountsRepository;
    private Deposit deposit;

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

        deposit = Deposit.create(accountId, 1.5, 6, BigDecimal.valueOf(2000));
    }


    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenCreated_thenCorrect() {
        var id = repository.create(deposit);
        assertNotNull(id);
    }

    @Test
    public void findById_whenCreatedAndFound_thenCorrect() {
        var id1 = repository.create(deposit);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void create_whenSavedAndRetrieved_thenValuesCorrect() {
        var id = repository.create(deposit);
        var retrieved = repository.findById(id).get();

        assertEquals(deposit.getMinimum().compareTo(retrieved.getMinimum()), 0);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(deposit);
        repository.create(deposit);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }
}
