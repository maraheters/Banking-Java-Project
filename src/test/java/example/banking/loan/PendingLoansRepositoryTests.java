package example.banking.loan;

import example.banking.RepositoryTest;
import example.banking.account.entity.Account;
import example.banking.account.repository.AccountsRepository;
import example.banking.account.repository.AccountsRepositoryImpl;
import example.banking.account.types.AccountType;
import example.banking.contracts.PendingEntityStatus;
import example.banking.loan.entity.PendingLoan;
import example.banking.loan.repository.PendingLoansRepository;
import example.banking.loan.repository.PendingLoansRepositoryImpl;
import example.banking.user.entity.Client;
import example.banking.user.repository.ClientsRepository;
import example.banking.user.repository.ClientsRepositoryImpl;
import example.banking.user.roles.ClientRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RepositoryTest
public class PendingLoansRepositoryTests {

    final ClientsRepository clientsRepository;
    final AccountsRepository accountsRepository;
    final PendingLoansRepository repository;
    PendingLoan loan1;
    PendingLoan loan2;
    Long clientId;

    @Autowired
    public PendingLoansRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new PendingLoansRepositoryImpl(template);
        this.clientsRepository = new ClientsRepositoryImpl(template);
        this.accountsRepository = new AccountsRepositoryImpl(template);
    }

    @BeforeEach
    public void setup() {
        var user = Client.register(
                "Joe",
                "+375282828",
                "12345",
                "12345",
                "email@email.com",
                "password",
                List.of(ClientRole.BASIC));

        clientId = clientsRepository.create(user);
        var account = Account.create(clientId, AccountType.PERSONAL);
        Long accountId = accountsRepository.create(account);

        loan1 = PendingLoan.create(accountId, BigDecimal.TEN, BigDecimal.valueOf(0.08), 12);
        loan2 = PendingLoan.create(accountId, BigDecimal.TEN, BigDecimal.valueOf(0.08), 12);
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

        var client = clientsRepository.findById(clientId).get();

        var allPendingLoans = repository.findAllByUserId(client.getUserId());

        assertEquals(2, allPendingLoans.size());
    }
}
