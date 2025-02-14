package example.banking.deposit;

import example.banking.account.entity.Account;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.repository.DepositRepository;
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
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepositRepositoryTests {


    private final DepositRepository repository;

    @Autowired
    public DepositRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new DepositRepository(template);
    }

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(repository);
    }

    @Test
    public void saveDeposit_whenSaved_thenCorrect() {
        var deposit = Deposit.create(new Account(), 2);

        var id = repository.save(deposit);
        Assertions.assertNotNull(id);
    }

    @Test
    public void saveDeposit_whenFindById_thenCorrect() {
        var id1 = repository.save( Deposit.create(new Account(), 2) );

        var id2 = repository.findById(id1).getId();

        Assertions.assertEquals(id1, id2);
    }


}
