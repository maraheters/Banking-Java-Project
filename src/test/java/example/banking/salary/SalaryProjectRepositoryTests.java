package example.banking.salary;

import example.banking.RepositoryTest;
import example.banking.salary.model.SalaryProject;
import example.banking.salary.repository.SalaryProjectRepository;
import example.banking.salary.repository.SalaryProjectRepositoryImpl;
import example.banking.salary.types.SalaryProjectStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RepositoryTest
@TestPropertySource(properties = """
    spring.flyway.locations=classpath:db/migration,\
                            classpath:db/seeders/bank,\
                            classpath:db/seeders/enterprise,\
                            classpath:db/seeders/specialist,\
                            classpath:db/seeders/enterpriseAccount
""")
public class SalaryProjectRepositoryTests {

    final SalaryProjectRepository repository;
    final SalaryProject salaryProject1;
    final SalaryProject salaryProject2;
    final Long enterpriseId = 1L;

    @Autowired
    public SalaryProjectRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new SalaryProjectRepositoryImpl(template);

        salaryProject1 = SalaryProject.register(enterpriseId, 1L);
        salaryProject2 = SalaryProject.register(enterpriseId, 2L);
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenCreated_thenCorrect() {
        var id = repository.create(salaryProject1);

        assertNotNull(id);
    }

    @Test
    public void update_whenStatusChanged_thenCorrect() {
        var id = repository.create(salaryProject1);

        var project = repository.findById(id).get();

        project.setActive();
        repository.update(project);

        var projectAfterUpdate = repository.findById(id).get();

        assertEquals(SalaryProjectStatus.ACTIVE, projectAfterUpdate.toDto().getStatus());
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(salaryProject1);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(salaryProject1);
        repository.create(salaryProject2);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    public void findAllByEnterpriseId_whenSavedAndRetrieved_thenCorrect() {
        repository.create(salaryProject1);
        repository.create(salaryProject2);

        var results = repository.findAllByEnterpriseId(enterpriseId);
        assertEquals(2, results.size());
    }

    @Test
    public void findAllBySpecialistId_whenSavedAndRetrieved_thenCorrect() {
        repository.create(salaryProject1);
        repository.create(salaryProject2);

        // Both projects belong to a specialist with id 100 (see R__6_add_enterprise_accounts)
        var results = repository.findAllBySpecialistId(100L);
        assertEquals(2, results.size());
    }

    @Test
    public void delete_whenDeleted_thenCorrect() {
        var id1 = repository.create(salaryProject1);
        var id2 = repository.create(salaryProject2);

        repository.delete(id1);

        var result = repository.findAll();

        assertEquals(result.size(), 1);
    }


}
