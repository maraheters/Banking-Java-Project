package example.banking.account.service;

import example.banking.account.dto.SalaryAccountRequestDto;
import example.banking.account.entity.SalaryAccount;
import example.banking.account.repository.SalaryAccountsRepository;
import example.banking.exception.ResourceNotFoundException;
import example.banking.salary.repository.SalaryProjectRepository;
import example.banking.security.BankingUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryAccountsService {

    private final SalaryAccountsRepository salaryAccountsRepository;
    private final SalaryProjectRepository salaryProjectRepository;

    @Autowired
    public SalaryAccountsService(
            SalaryAccountsRepository salaryAccountsRepository,
            SalaryProjectRepository salaryProjectRepository) {
        this.salaryAccountsRepository = salaryAccountsRepository;
        this.salaryProjectRepository = salaryProjectRepository;
    }

    public SalaryAccount getById(Long id) {
        return salaryAccountsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary account with id '" + id + "' not found."));
    }

    public List<SalaryAccount> getAll() {
        return salaryAccountsRepository.findAll();
    }

    public List<SalaryAccount> getAllBySalaryProjectId(Long id) {
        return salaryAccountsRepository.findAllBySalaryProjectId(id);
    }

    public Long create(BankingUserDetails userDetails, SalaryAccountRequestDto dto) {

        var account = SalaryAccount.create(dto.getHolderId(), dto.getBankId(), dto.getSalaryProjectId(), dto.getSalary());

        return salaryAccountsRepository.create(account);
    }

    public boolean validateOwner(Long id, BankingUserDetails userDetails) {
        var account = getById(id);
        return account.isOwner(userDetails.getId());
    }
}
