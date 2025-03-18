package example.banking.account;

import example.banking.RepositoryTest;
import example.banking.account.entity.SalaryAccount;
import example.banking.account.repository.SalaryAccountsRepositoryImpl;
import example.banking.account.repository.SalaryAccountsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RepositoryTest
@TestPropertySource(properties = """
    spring.flyway.locations=classpath:db/migration,\
                            classpath:db/seeders/bank,\
                            classpath:db/seeders/client,\
                            classpath:db/seeders/enterprise,\
                            classpath:db/seeders/specialist,\
                            classpath:db/seeders/enterpriseAccount,\
                            classpath:db/seeders/salary
""")
public class SalaryAccountsRepositoryTests {

    final SalaryAccountsRepository repository;
    final SalaryAccount account1;
    final SalaryAccount account2;

    @Autowired
    public SalaryAccountsRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new SalaryAccountsRepositoryImpl(template);

        account1 = SalaryAccount.create(1L, 1L, 1L, BigDecimal.TEN);
        account2 = SalaryAccount.create(1L, 1L, 1L, BigDecimal.TWO);
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

        repository.update(SalaryAccount.fromDto(accountDto));

        var balanceAfter = repository.findById(id).get().toDto().getBalance();

        assertEquals(balanceAfter.doubleValue(), expectedBalanceAfter.doubleValue(), 0.001);
    }

    @Test
    public void findBySalaryProjectId_whenFound_thenCorrect() {
        repository.create( account1 );
        repository.create( account2 );

        var result = repository.findAllBySalaryProjectId(1L);

        assertEquals(result.size(), 2);
    }

    @Test
    public void batchCreate_whenSaved_thenCorrect() {
        var results = repository.batchCreate(List.of(account1, account2));

        assertEquals(2, results.size());
    }

}
