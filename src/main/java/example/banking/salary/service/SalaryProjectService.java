package example.banking.salary.service;

import example.banking.account.entity.EnterpriseAccount;
import example.banking.account.entity.SalaryAccount;
import example.banking.account.repository.EnterpriseAccountsRepository;
import example.banking.account.repository.SalaryAccountsRepository;
import example.banking.exception.BadRequestException;
import example.banking.exception.ResourceNotFoundException;
import example.banking.salary.dto.SalaryProjectCreatedResponseDto;
import example.banking.salary.dto.SalaryProjectRequestDto;
import example.banking.salary.model.SalaryProject;
import example.banking.salary.repository.SalaryProjectRepository;
import example.banking.salary.types.SalaryProjectStatus;
import example.banking.security.BankingUserDetails;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.transaction.types.TransactionType;
import example.banking.user.repository.SpecialistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryProjectService {

    private final SalaryProjectRepository salaryProjectRepository;
    private final EnterpriseAccountsRepository enterpriseAccountsRepository;
    private final SalaryAccountsRepository salaryAccountsRepository;
    private final SpecialistsRepository specialistsRepository;
    private final TransactionsRepository transactionsRepository;

    @Autowired
    public SalaryProjectService(
            SalaryProjectRepository salaryProjectRepository,
            EnterpriseAccountsRepository enterpriseAccountsRepository,
            SalaryAccountsRepository salaryAccountsRepository,
            SpecialistsRepository specialistsRepository,
            TransactionsRepository transactionsRepository) {

        this.salaryProjectRepository = salaryProjectRepository;
        this.enterpriseAccountsRepository = enterpriseAccountsRepository;
        this.salaryAccountsRepository = salaryAccountsRepository;
        this.specialistsRepository = specialistsRepository;
        this.transactionsRepository = transactionsRepository;
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

    @Transactional
    public BigDecimal paySalary(Long projectId) {
        var project = getById(projectId);

        if (!project.isStatus(SalaryProjectStatus.ACTIVE)) {
            throw new BadRequestException("Project status must be ACTIVE.");
        }

        var enterpriseAccount = enterpriseAccountsRepository.findById(project.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + project.getAccountId() + "' not found."));

        var totalSalaryToPay = project.getTotalSalary();
        enterpriseAccount.withdraw(totalSalaryToPay);
        enterpriseAccountsRepository.update(enterpriseAccount);

        var salaryAccounts = salaryAccountsRepository.findAllBySalaryProjectId(projectId);
        var salaryAccountsToUpdate = new ArrayList<SalaryAccount>();
        var transactionsToCreate = new ArrayList<Transaction>();

        for (var account : salaryAccounts) {
            var salary = account.getSalary();
            account.topUp(salary);
            salaryAccountsToUpdate.add(account);

            var transaction = Transaction.create(
                    enterpriseAccount.getId(), TransactionType.ACCOUNT, account.getId(), TransactionType.ACCOUNT, salary
            );
            transactionsToCreate.add(transaction);
        }
        salaryAccountsRepository.batchUpdate(salaryAccountsToUpdate);
        transactionsRepository.batchCreate(transactionsToCreate);

        return totalSalaryToPay;
    }

    @Transactional
//    @Scheduled(fixedDelay = 10000)
    protected void applyMonthlySalary() {
        var activeProjects = salaryProjectRepository.findAll()
                .stream()
                .filter(sp -> sp.isStatus(SalaryProjectStatus.ACTIVE))
                .toList();

        var salaryAccountsToUpdate = new ArrayList<SalaryAccount>();
        var enterpriseAccountsToUpdate = new ArrayList<EnterpriseAccount>();
        for (var project : activeProjects) {

            var salaryAccounts = salaryAccountsRepository.findAllBySalaryProjectId(project.getId());
            for (var account : salaryAccounts) {
                account.topUp(account.getSalary());
                salaryAccountsToUpdate.add(account);
            }

            var enterpriseAccount = enterpriseAccountsRepository.findById(project.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + project.getAccountId() + "' not found."));

            var totalSalaryToPay = project.getTotalSalary();
            enterpriseAccount.withdraw(totalSalaryToPay);
            enterpriseAccountsToUpdate.add(enterpriseAccount);
        }

        salaryAccountsRepository.batchUpdate(salaryAccountsToUpdate);
        enterpriseAccountsRepository.batchUpdate(enterpriseAccountsToUpdate);
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
