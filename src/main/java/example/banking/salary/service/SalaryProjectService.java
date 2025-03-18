package example.banking.salary.service;

import example.banking.account.repository.EnterpriseAccountsRepository;
import example.banking.exception.ResourceNotFoundException;
import example.banking.salary.dto.SalaryProjectRequestDto;
import example.banking.salary.model.SalaryProject;
import example.banking.salary.repository.SalaryProjectRepository;
import example.banking.security.BankingUserDetails;
import example.banking.user.repository.ClientsRepository;
import example.banking.user.repository.SpecialistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryProjectService {

    private final SalaryProjectRepository salaryProjectRepository;
    private final EnterpriseAccountsRepository enterpriseAccountsRepository;
    private final SpecialistsRepository specialistsRepository;
    private final ClientsRepository clientsRepository;

    @Autowired
    public SalaryProjectService(
            SalaryProjectRepository salaryProjectRepository,
            EnterpriseAccountsRepository enterpriseAccountsRepository,
            SpecialistsRepository specialistsRepository,
            ClientsRepository clientsRepository) {

        this.salaryProjectRepository = salaryProjectRepository;
        this.enterpriseAccountsRepository = enterpriseAccountsRepository;
        this.specialistsRepository = specialistsRepository;
        this.clientsRepository = clientsRepository;
    }

    public List<SalaryProject> getAll() {
        return salaryProjectRepository.findAll();
    }

    public List<SalaryProject> getAllByEnterpriseId(Long enterpriseId) {
        return salaryProjectRepository.findAllByEnterpriseId(enterpriseId);
    }

    public SalaryProject getById(Long id) {
        return salaryProjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary project with id '" + id + "' not found"));
    }

    public Long create(BankingUserDetails userDetails, SalaryProjectRequestDto dto) {
        var specialist = specialistsRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise specialist with id '" + userDetails.getId() + "' not found."));

        var project = SalaryProject.register(specialist.getEnterpriseId(), dto.getAccountId());

        return salaryProjectRepository.create(project);
    }

    public void approve(Long id) {
        var project = getById(id);
        project.setActive();
        salaryProjectRepository.update(project);
    }

    public void dismiss(Long id) {
        var project = getById(id);

        salaryProjectRepository.delete(id);
    }

    public boolean verifySpecialist(Long projectId, BankingUserDetails userDetails) {
        var project = getById(projectId);

        return project.verifySpecialist(userDetails.getId());
    }

}
