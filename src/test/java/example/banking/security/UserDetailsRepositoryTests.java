package example.banking.security;

import example.banking.security.repository.UserDetailsRepository;
import example.banking.security.repository.UserDetailsRepositoryImpl;
import example.banking.user.entity.Client;
import example.banking.user.repository.ClientsRepository;
import example.banking.user.repository.ClientsRepositoryImpl;
import example.banking.user.roles.ClientRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Testcontainers
@ActiveProfiles("test-containers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:/userRoles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserDetailsRepositoryTests {

    private final UserDetailsRepository repository;
    private final ClientsRepository clientsRepository;

    private final String email = "email";
    private final String passwordHash = "password";

    @Autowired
    public UserDetailsRepositoryTests(NamedParameterJdbcTemplate jdbcTemplate) {
        this.repository = new UserDetailsRepositoryImpl(jdbcTemplate);
        this.clientsRepository = new ClientsRepositoryImpl(jdbcTemplate);
    }

    @BeforeEach
    public void setUp() {
        var client = Client.register(
                "Name",
                "123",
                "123",
                "123",
                email,
                passwordHash,
                List.of(ClientRole.BASIC));

        clientsRepository.create(client);
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void findByEmail_whenFound_thenNotNull() {
        var optional = repository.findByEmail(email);

        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
    }

    @Test
    public void findByEmail_whenFound_thenValuesCorrect() {
        var user = repository.findByEmail(email).get();

        assertEquals(user.getPassword(), passwordHash);
        assertEquals(user.getUsername(), email);
    }


}
