package example.banking.account;

import example.banking.RepositoryTest;
import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.repository.AccountsRepositoryImpl;
import example.banking.account.types.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
@TestPropertySource(properties = """
    spring.flyway.locations=classpath:db/migration,\
                            classpath:db/seeders/bank,\
                            classpath:db/seeders/client
""")
public class AccountsRepositoryTests {

    private final AccountsRepository repository;
    private final Long clientId = 1L;
    private Account account1;
    private Account account2;

    @Autowired
    public AccountsRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new AccountsRepositoryImpl(template);
    }

    @BeforeEach
    public void setUp() {

        account1 = Account.create(clientId, 1L, AccountType.PERSONAL);
        account2 = Account.create(clientId, 1L, AccountType.PERSONAL);
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenSaved_thenCorrect() {
        var id = repository.create(account1);
        assertNotNull(id);
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(account1);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create( account1 );
        repository.create( account2 );

        var results = repository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    public void update_whenUpdated_thenCorrect() {
        var id = repository.create( account1 );

        var accountDto = repository.findById(id).get().toDto();
        var expectedBalanceAfter = BigDecimal.valueOf(29321);
        accountDto.setBalance(expectedBalanceAfter);

        repository.update(Account.fromDto(accountDto));

        var balanceAfter = repository.findById(id).get().toDto().getBalance();

        assertEquals(balanceAfter.doubleValue(), expectedBalanceAfter.doubleValue(), 0.001);
    }

    @Test
    public void findByHolderId_whenFound_thenCorrect() {
        repository.create( account1 );

        var result = repository.findByHolderId(clientId);

        assertFalse(result.isEmpty());
    }

    @Test
    public void findByUserId_whenFound_thenCorrect() {
        repository.create( account1 );

        Long userId = 1L;
        var result = repository.findByUserId(userId);

        assertFalse(result.isEmpty());
    }
}
