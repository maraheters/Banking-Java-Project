package example.banking.bank;

import example.banking.RepositoryTest;
import example.banking.bank.model.Bank;
import example.banking.bank.repository.BanksRepository;
import example.banking.bank.repository.BanksRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RepositoryTest
public class BanksRepositoryTests {

    private final BanksRepository repository;
    private final Bank bank1;
    private final Bank bank2;

    @Autowired
    public BanksRepositoryTests(NamedParameterJdbcTemplate template) {
        repository = new BanksRepositoryImpl(template);

        bank1 = Bank.create("bank1", "123", "big city");
        bank2 = Bank.create("bank2", "2345", "bigger city");
    }

    @Test
    public void contextLoads() {
        assertNotNull(repository);
    }

    @Test
    public void create_whenSaved_thenCorrect() {
        var id = repository.create(bank1);
        assertNotNull(id);
    }

    @Test
    public void findById_whenSavedAndRetrieved_thenCorrect() {
        var id1 = repository.create(bank1);

        var id2 = repository.findById(id1).get().getId();

        assertEquals(id1, id2);
    }

    @Test
    public void findAll_whenSavedAndRetrieved_thenCorrect() {
        repository.create(bank1);
        repository.create(bank2);

        var results = repository.findAll();
        assertEquals(2, results.size());
    }
}
