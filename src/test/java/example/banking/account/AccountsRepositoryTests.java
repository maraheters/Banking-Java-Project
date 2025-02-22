package example.banking.account;

import example.banking.account.dto.AccountDto;
import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.repository.AccountsRepositoryImpl;
import example.banking.account.types.AccountType;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Testcontainers
@ActiveProfiles("test-containers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountsRepositoryTests {

    private final AccountsRepository repository;
    private final UsersRepository usersRepository;
    private final Account account1;
    private final Account account2;

    @Autowired
    public AccountsRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new AccountsRepositoryImpl(template);
        usersRepository = new UsersRepositoryImpl(template);
        account1 = Account.create(22L, AccountType.PERSONAL);
        account2 = Account.create(22L, AccountType.PERSONAL);
    }

//    @BeforeEach
//    public void setUp() {
//
//    }

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
}
