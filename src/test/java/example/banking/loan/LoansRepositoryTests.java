package example.banking.loan;

import example.banking.RepositoryTest;
import example.banking.loan.entity.Loan;
import example.banking.loan.repository.LoansRepository;
import example.banking.loan.repository.LoansRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RepositoryTest
@TestPropertySource(properties = """
    spring.flyway.locations=classpath:db/migration,\
                            classpath:db/seeders/bank,\
                            classpath:db/seeders/client,\
                            classpath:db/seeders/account
""")
public class LoansRepositoryTests {

    final LoansRepository repository;
    final Loan loan1;
    final Loan loan2;

    @Autowired
    public LoansRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new LoansRepositoryImpl(template);
        this.loan1 = Loan.create(1L, BigDecimal.TEN, BigDecimal.valueOf(0.05), 12);
        this.loan2 = Loan.create(1L, BigDecimal.TEN, BigDecimal.valueOf(0.05), 12);
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenSaved_thenCorrect() {
        var id = repository.create(loan1);
        assertNotNull(id);
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(loan1);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(loan1);
        repository.create(loan2);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    public void update_whenPaidAmountUpdated_thenCorrect() {
        var id = repository.create(loan1);
        var loan = repository.findById(id).get();

        loan.makePayment(BigDecimal.valueOf(5));

        repository.update(loan);

        var loanAfterUpdate = repository.findById(id).get();

        assertEquals(BigDecimal.valueOf(5).intValue(), loanAfterUpdate.toDto().getPaidAmount().intValue());
    }

    @Test
    void findAllByUserId_whenFound_thenCorrect() {
        repository.create(loan1);
        repository.create(loan2);

        var allPendingLoans = repository.findAllByUserId(1L);

        assertEquals(2, allPendingLoans.size());
    }
}
