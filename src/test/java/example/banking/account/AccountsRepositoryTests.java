package example.banking.account;

import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.repository.AccountsRepositoryImpl;
import example.banking.account.types.AccountType;
import example.banking.user.entity.Client;
import example.banking.user.repository.ClientsRepository;
import example.banking.user.repository.ClientsRepositoryImpl;
import example.banking.user.roles.ClientRole;
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
public class AccountsRepositoryTests {

    private final AccountsRepository repository;
    private final ClientsRepository clientsRepository;
    private Account account1;
    private Account account2;
    private Long clientId;

    @Autowired
    public AccountsRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new AccountsRepositoryImpl(template);
        clientsRepository = new ClientsRepositoryImpl(template);
    }

    @BeforeEach
    public void setUp() {
        var client = Client.register("Name", "phone", "passport",
                "identification", "email", "password", List.of(ClientRole.BASIC));

        clientId = clientsRepository.create(client);

        account1 = Account.create(clientId, AccountType.PERSONAL);
        account2 = Account.create(clientId, AccountType.PERSONAL);
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
}
