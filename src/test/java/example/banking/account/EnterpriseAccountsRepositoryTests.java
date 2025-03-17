package example.banking.account;

import example.banking.RepositoryTest;
import example.banking.account.entity.EnterpriseAccount;
import example.banking.account.repository.EnterpriseAccountsRepository;
import example.banking.account.repository.EnterpriseAccountsRepositoryImpl;
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
                            classpath:db/seeders/enterprise,\
                            classpath:db/seeders/specialist
""")
public class EnterpriseAccountsRepositoryTests {

    private final EnterpriseAccountsRepository repository;
    private final Long enterpriseId = 1L;
    private final Long specialistId = 1L;
    private final EnterpriseAccount account1;
    private final EnterpriseAccount account2;

    @Autowired
    public EnterpriseAccountsRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new EnterpriseAccountsRepositoryImpl(template);

        account1 = EnterpriseAccount.create(enterpriseId, specialistId, 1L);
        account2 = EnterpriseAccount.create(enterpriseId, specialistId, 1L);
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

        repository.update(EnterpriseAccount.fromDto(accountDto));

        var balanceAfter = repository.findById(id).get().toDto().getBalance();

        assertEquals(balanceAfter.doubleValue(), expectedBalanceAfter.doubleValue(), 0.001);
    }

    @Test
    public void findByEnterpriseId_whenFound_thenCorrect() {
        repository.create( account1 );
        repository.create( account2 );

        var result = repository.findAllByEnterpriseId(enterpriseId);

        assertEquals(result.size(), 2);
    }

    @Test
    public void findBySpecialistId_whenFound_thenCorrect() {
        repository.create( account1 );

        var result = repository.findAllBySpecialistId(enterpriseId);

        assertEquals(result.size(), 1);
    }
}
