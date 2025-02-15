package example.banking.account;

import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.types.AccountType;
import example.banking.deposit.entity.Deposit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@Testcontainers
@ActiveProfiles("test-containers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountsRepositoryTests {

    private final AccountsRepository repository;

    @Autowired
    public AccountsRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new AccountsRepository(template);
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(repository);
    }

    @Test
    public void createAccount_whenSaved_thenCorrect() {
        var account = Account.create(null, AccountType.PERSONAL);

        var id = repository.create(account);
        Assertions.assertNotNull(id);
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(Account.create(null, AccountType.PERSONAL));

        var id2 = repository.findById(id1).get().getId();

        Assertions.assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create( Account.create(null, AccountType.PERSONAL) );
        repository.create( Account.create(null, AccountType.PERSONAL) );

        var results = repository.findAll();
        Assertions.assertEquals(2, results.size());
    }
}
