package example.banking.user;

import example.banking.RepositoryTest;
import example.banking.user.entity.Supervisor;
import example.banking.user.repository.SupervisorsRepository;
import example.banking.user.repository.SupervisorsRepositoryImpl;
import example.banking.user.roles.SupervisorRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RepositoryTest
public class SupervisorsRepositoryTests {

    private final SupervisorsRepository repository;
    private final Supervisor supervisor1;
    private final Supervisor supervisor2;

    private final List<SupervisorRole> supervisorRoles1 =
            List.of(SupervisorRole.ADMINISTRATOR, SupervisorRole.MANAGER);
    private final List<SupervisorRole> supervisorRoles2 =
            List.of(SupervisorRole.MANAGER);

    @Autowired
    public SupervisorsRepositoryTests(NamedParameterJdbcTemplate template) {
        this.repository = new SupervisorsRepositoryImpl(template);

        supervisor1 = Supervisor.register(
                "John Doe",
                "john@doe",
                "password",
                supervisorRoles1
        );

        supervisor2 = Supervisor.register(
                "Bim Bam",
                "bim@bam",
                "password",
                supervisorRoles2
        );
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenSaved_thenCorrect() {
        var id = repository.create(supervisor1);
        assertNotNull(id);
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(supervisor1);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(supervisor1);
        repository.create(supervisor2);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    public void create_findBy_id_whenSavedAndRetrieved_thenRolesCorrect() {
        var id = repository.create(supervisor1);

        var supervisor = repository.findById(id).get();

        //convert to hashsets because order of roles does not matter
        assertEquals(new HashSet<>(supervisorRoles1), new HashSet<>(supervisor.toDto().getRoles()));
    }
}
