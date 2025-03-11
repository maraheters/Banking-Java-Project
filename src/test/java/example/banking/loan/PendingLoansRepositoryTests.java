package example.banking.loan;

import example.banking.RepositoryTest;
import example.banking.contracts.PendingEntityStatus;
import example.banking.loan.entity.PendingLoan;
import example.banking.loan.repository.PendingLoansRepository;
import example.banking.loan.repository.PendingLoansRepositoryImpl;
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
public class PendingLoansRepositoryTests {

    final PendingLoansRepository repository;
    final PendingLoan loan1;
    final PendingLoan loan2;

    @Autowired
    public PendingLoansRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new PendingLoansRepositoryImpl(template);

        loan1 = PendingLoan.create(1L, BigDecimal.TEN, BigDecimal.valueOf(0.08), 12);
        loan2 = PendingLoan.create(1L, BigDecimal.TEN, BigDecimal.valueOf(0.08), 12);
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
    public void update_whenStatusUpdated_thenCorrect() {
        // Status is PENDING by default
        var id = repository.create(loan1);
        var loan = repository.findById(id).get();

        loan.setApproved();

        repository.update(loan);

        var loanAfterUpdate = repository.findById(id).get();

        assertEquals(PendingEntityStatus.APPROVED, loanAfterUpdate.toDto().getStatus());
    }

    @Test
    void findAllByUserId_whenFound_thenCorrect() {
        repository.create(loan1);
        repository.create(loan2);

        var allPendingLoans = repository.findAllByUserId(1L);

        assertEquals(2, allPendingLoans.size());
    }
}
