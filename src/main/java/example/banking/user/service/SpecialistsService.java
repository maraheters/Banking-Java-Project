package example.banking.user.service;

import example.banking.exception.ResourceNotFoundException;
import example.banking.user.entity.Specialist;
import example.banking.user.repository.SpecialistsRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialistsService {

    private final SpecialistsRepositoryImpl repository;

    @Autowired
    public SpecialistsService(SpecialistsRepositoryImpl repository) {
        this.repository = repository;
    }

    public List<Specialist> getAll() {
        return repository.findAll();
    }

    public Specialist getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist with id '" + id + "' not found"));
    }
}
