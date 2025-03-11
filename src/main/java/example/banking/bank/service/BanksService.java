package example.banking.bank.service;

import example.banking.bank.model.Bank;
import example.banking.bank.repository.BanksRepository;
import example.banking.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BanksService {

    private final BanksRepository repository;

    @Autowired
    public BanksService(BanksRepository repository) {
        this.repository = repository;
    }

    public List<Bank> getAll() {
        return repository.findAll();
    }

    public Bank getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank with id '" + id + "' not found."));
    }

    public Long create(String name, String bic, String address) {
        var bank = Bank.create(name, bic, address);

        return repository.create(bank);
    }
}
