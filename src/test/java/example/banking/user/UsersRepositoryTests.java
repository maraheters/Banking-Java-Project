package example.banking.user;

import example.banking.user.entity.User;
import example.banking.user.repository.UsersRepository;
import example.banking.user.repository.UsersRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Testcontainers
@ActiveProfiles("test-containers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsersRepositoryTests {

    private final UsersRepository repository;
    private final User user1;
    private final User user2;

    @Autowired
    public UsersRepositoryTests(NamedParameterJdbcTemplate jdbcTemplate) {
        repository = new UsersRepositoryImpl(jdbcTemplate);
        user1 = User.create(
                "Joe", "+375282828", "12345", "12345", "email@email.com");
        user2 = User.create(
                "Joe", "+3752828428", "123435", "124345", "emaile@email.com");
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenSaved_thenCorrect() {
        var id = repository.create(user1);
        assertNotNull(id);
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(user1);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(user1);
        repository.create(user2);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }
}
