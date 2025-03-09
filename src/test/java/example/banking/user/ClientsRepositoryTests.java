package example.banking.user;

import example.banking.RepositoryTest;
import example.banking.user.entity.Client;
import example.banking.user.repository.ClientsRepository;
import example.banking.user.repository.ClientsRepositoryImpl;
import example.banking.user.roles.ClientRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
public class ClientsRepositoryTests {

    private final ClientsRepository repository;
    private final Client client1;
    private final Client client2;

    @Autowired
    public ClientsRepositoryTests(NamedParameterJdbcTemplate jdbcTemplate) {
        repository = new ClientsRepositoryImpl(jdbcTemplate);

        client1 = Client.register(
                "Joe",
                "+375282828",
                "12345",
                "12345",
                "email@email.com",
                "password",
                List.of(ClientRole.BASIC));

        client2 = Client.register(
                "Joe",
                "+3752828428",
                "123435",
                "124345",
                "emaile@email.com",
                "password",
                List.of(ClientRole.BASIC));
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
        var id = repository.create(client1);

        var user = repository.findByEmail(email).get();

        assertEquals(id, user.getId());
        assertEquals(email, user.toDto().getEmail());
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(client1);
        repository.create(client2);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    public void create_findBy_id_whenSavedAndRetrieved_thenRolesCorrect() {
        var id = repository.create(client1);

        var client = repository.findById(id).get();

        assertEquals(List.of(ClientRole.BASIC), client.toDto().getRoles());
    }

}
