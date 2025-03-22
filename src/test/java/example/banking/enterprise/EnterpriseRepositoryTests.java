package example.banking.enterprise;

import example.banking.RepositoryTest;
import example.banking.enterprise.model.Enterprise;
import example.banking.enterprise.repository.EnterpriseRepository;
import example.banking.enterprise.repository.EnterpriseRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RepositoryTest
@TestPropertySource(properties = """
    spring.flyway.locations=classpath:db/migration,\
                            classpath:db/seeders/bank
""")
public class EnterpriseRepositoryTests {

    private final EnterpriseRepository repository;
    private final Enterprise enterprise1;
    private final Enterprise enterprise2;

    @Autowired
    public EnterpriseRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new EnterpriseRepositoryImpl(template);

        enterprise1 = Enterprise.create("type", "legalName1", "unp1", 1L, "legalAddress1");
        enterprise2 = Enterprise.create("type", "legalName2", "unp2", 1L, "legalAddress2");
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenSaved_thenCorrect() {
        var id = repository.create(enterprise1);

        assertNotNull(id);
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(enterprise1);
        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(enterprise1);
        repository.create(enterprise2);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }
}
