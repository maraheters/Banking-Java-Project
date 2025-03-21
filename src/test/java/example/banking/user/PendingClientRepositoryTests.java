package example.banking.user;

import example.banking.RepositoryTest;
import example.banking.contracts.PendingEntityStatus;
import example.banking.user.entity.PendingClient;
import example.banking.user.repository.PendingClientsRepository;
import example.banking.user.repository.PendingClientsRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
public class PendingClientRepositoryTests {

    private final PendingClientsRepository repository;
    private final PendingClient client1;
    private final PendingClient client2;

    @Autowired
    public PendingClientRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new PendingClientsRepositoryImpl(template);

        client1 = PendingClient.register(
                "Joe",
                "+375282828",
                "12345",
                "12345",
                "email@email.com",
                "password"
        );

        client2 = PendingClient.register(
                "Joe",
                "+3752828428",
                "123435",
                "124345",
                "emaile@email.com",
                "password"
        );
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenSaved_thenCorrect() {
        var id = repository.create(client1);
        assertNotNull(id);
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(client1);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findByEmail_whenSavedAndRetrieved_thenCorrect() {
        var email = client1.toDto().getEmail();

        var id1 = repository.create(client1);

        var id2 = repository.findByEmail(email).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(client1);
        repository.create(client2);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    public void delete_whenDeleted_thenCorrect() {
        var id1 = repository.create(client1);
        var id2 = repository.create(client2);

        repository.delete(id1);

        var results = repository.findAll();
        assertEquals(1, results.size());
        assertEquals(id2, results.getFirst().getId());
    }

    @Test
    public void update_whenStatusChanged_thenCorrect() {
        var id1 = repository.create(client1);

        var client = repository.findById(id1).get();

        client.setApproved();

        repository.update(client);

        var clientAfterUpdate = repository.findById(id1).get();

        assertEquals(clientAfterUpdate.toDto().getStatus(), PendingEntityStatus.APPROVED);
    }
}
