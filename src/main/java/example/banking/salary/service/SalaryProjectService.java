package example.banking.salary.service;

import example.banking.account.entity.SalaryAccount;
import example.banking.account.repository.EnterpriseAccountsRepository;
import example.banking.account.repository.SalaryAccountsRepository;
import example.banking.exception.ResourceNotFoundException;
import example.banking.salary.dto.SalaryProjectCreatedResponseDto;
import example.banking.salary.dto.SalaryProjectRequestDto;
import example.banking.salary.model.SalaryProject;
import example.banking.salary.repository.SalaryProjectRepository;
import example.banking.security.BankingUserDetails;
import example.banking.user.repository.ClientsRepository;
import example.banking.user.repository.SpecialistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryProjectService {

    private final SalaryProjectRepository salaryProjectRepository;
    private final EnterpriseAccountsRepository enterpriseAccountsRepository;
    private final SalaryAccountsRepository salaryAccountsRepository;
    private final SpecialistsRepository specialistsRepository;
    private final ClientsRepository clientsRepository;

    @Autowired
    public SalaryProjectService(
            SalaryProjectRepository salaryProjectRepository,
            EnterpriseAccountsRepository enterpriseAccountsRepository,
            SalaryAccountsRepository salaryAccountsRepository,
            SpecialistsRepository specialistsRepository,
            ClientsRepository clientsRepository) {

        this.salaryProjectRepository = salaryProjectRepository;
        this.enterpriseAccountsRepository = enterpriseAccountsRepository;
        this.salaryAccountsRepository = salaryAccountsRepository;
        this.specialistsRepository = specialistsRepository;
        this.clientsRepository = clientsRepository;
    }

    public List<SalaryProject> getAll() {
        return salaryProjectRepository.findAll();
    }

    public List<SalaryProject> getAllByEnterpriseId(Long enterpriseId) {
        return salaryProjectRepository.findAllByEnterpriseId(enterpriseId);
    }

    public List<SalaryProject> getAllBySpecialistId(Long enterpriseId) {
        return salaryProjectRepository.findAllBySpecialistId(enterpriseId);
    }

    public SalaryProject getById(Long id) {
        return salaryProjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary project with id '" + id + "' not found"));
    }

    @Transactional
    public SalaryProjectCreatedResponseDto create(BankingUserDetails userDetails, SalaryProjectRequestDto dto) {
        var specialist = specialistsRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise specialist with id '" + userDetails.getId() + "' not found."));

        var enterpriseAccount = enterpriseAccountsRepository.findById(dto.getEnterpriseAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Enterprise account with id '" + dto.getEnterpriseAccountId() + "' not found."));

        var project = SalaryProject.register(specialist.getEnterpriseId(), enterpriseAccount.getId());

        var salaryProjectId = salaryProjectRepository.create(project);


        var accountRequests = dto.getAccountRequestDtos();
        var createdAccounts = new ArrayList<SalaryAccount>();

        for(var request : accountRequests) {
            var account = SalaryAccount.create(
                    request.getHolderId(),
                    enterpriseAccount.getBankId(),
                    salaryProjectId,
                    request.getSalary()
            );

            createdAccounts.add(account);
        }

        var createdAccountIds = salaryAccountsRepository.batchCreate(createdAccounts);

        return new SalaryProjectCreatedResponseDto(salaryProjectId, createdAccountIds);
    }

    public void approve(Long id) {
        var project = getById(id);
        project.setActive();
        salaryProjectRepository.update(project);
    }

    public void reject(Long id) {
        var project = getById(id);

        salaryProjectRepository.delete(id);
    }

    public boolean verifySpecialist(Long projectId, BankingUserDetails userDetails) {
        var project = getById(projectId);

        return project.verifySpecialist(userDetails.getId());
    }

}
