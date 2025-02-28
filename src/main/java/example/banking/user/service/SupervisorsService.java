package example.banking.user.service;

import example.banking.exception.ResourceNotFoundException;
import example.banking.user.entity.Supervisor;
import example.banking.user.repository.SupervisorsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorsService {

    private final SupervisorsRepository repository;

    public SupervisorsService(SupervisorsRepository repository) {
        this.repository = repository;
    }

    public List<Supervisor> getAll() {
        return repository.findAll();
    }

    public Supervisor getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supervisor with id '" + id + "' not found."));
    }
}
