package example.banking.security;

import example.banking.RepositoryTest;
import example.banking.security.repository.UserDetailsRepository;
import example.banking.security.repository.UserDetailsRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@RepositoryTest
@TestPropertySource(properties = """
    spring.flyway.locations=classpath:db/migration,\
                            classpath:db/seeders/client
""")
public class UserDetailsRepositoryTests {

    private final UserDetailsRepository repository;

    private final String email = "email1";
    private final String passwordHash = "password1";

    @Autowired
    public UserDetailsRepositoryTests(NamedParameterJdbcTemplate jdbcTemplate) {
        this.repository = new UserDetailsRepositoryImpl(jdbcTemplate);
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
