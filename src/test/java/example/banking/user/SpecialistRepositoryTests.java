package example.banking.user;

import example.banking.RepositoryTest;
import example.banking.user.entity.Specialist;
import example.banking.user.repository.SpecialistsRepository;
import example.banking.user.repository.SpecialistsRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RepositoryTest
@TestPropertySource(properties = """
    spring.flyway.locations=classpath:db/migration,\
                            classpath:db/seeders/bank,\
                            classpath:db/seeders/enterprise
""")
public class SpecialistRepositoryTests {

    final SpecialistsRepository repository;
    final Specialist specialist1;
    final Specialist specialist2;

    @Autowired
    public SpecialistRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new SpecialistsRepositoryImpl(template);

        specialist1 = Specialist.register("name1", "email1", "pword", 1L);
        specialist2 = Specialist.register("name2", "email2", "pword", 1L);
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenSaved_thenCorrect() {
        var id = repository.create(specialist1);
        assertNotNull(id);
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(specialist1);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findByEmail_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(specialist1);

        var id2 = repository.findByEmail(specialist1.toDto().getEmail()).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(specialist1);
        repository.create(specialist2);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }
}
