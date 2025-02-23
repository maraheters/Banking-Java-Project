package example.banking.user.service;

import example.banking.exception.ResourceNotFoundException;
import example.banking.user.dto.supervisor.SupervisorRegisterRequestDto;
import example.banking.user.entity.Supervisor;
import example.banking.user.repository.SupervisorsRepository;
import example.banking.user.roles.SupervisorRole;
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

    public Long register(SupervisorRegisterRequestDto requestDto) {

        Long companyId = requestDto.getCompanyId();

        if (companyId != null) {
            // add logic for checking later
        }

        List<SupervisorRole> roles = requestDto.getRoles().stream()
                .map(SupervisorRole::valueOf)
                .toList();

        var supervisor = Supervisor.register(
                requestDto.getName(), requestDto.getEmail(), requestDto.getPasswordHash(), roles, companyId
        );

        return repository.create(supervisor);
    }
}
