package example.banking.enterprise.service;

import example.banking.enterprise.model.Enterprise;
import example.banking.enterprise.repository.EnterpriseRepository;
import example.banking.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnterpriseService {

    private final EnterpriseRepository repository;

    @Autowired
    public EnterpriseService(EnterpriseRepository repository) {
        this.repository = repository;
    }

    public List<Enterprise> getAll() {
        return repository.findAll();
    }

    public Enterprise getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise with id '" + id + "' not found."));
    }

    public Long create(
            String type, String legalName, String unp, Long bankId, String legalAddress) {

        var enterprise = Enterprise.create(type, legalName, unp, bankId, legalAddress);

        return repository.create(enterprise);
    }
}
