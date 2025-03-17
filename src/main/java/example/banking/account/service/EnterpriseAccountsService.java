package example.banking.account.service;

import example.banking.account.entity.EnterpriseAccount;
import example.banking.account.repository.EnterpriseAccountsRepository;
import example.banking.enterprise.repository.EnterpriseRepository;
import example.banking.exception.BadRequestException;
import example.banking.exception.ResourceNotFoundException;
import example.banking.security.BankingUserDetails;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.user.repository.SpecialistsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnterpriseAccountsService {

    private final EnterpriseAccountsRepository accountsRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final SpecialistsRepository specialistsRepository;
    private final TransactionsRepository transactionsRepository;

    @Autowired
    public EnterpriseAccountsService(
            EnterpriseAccountsRepository accountsRepository,
            EnterpriseRepository enterpriseRepository,
            SpecialistsRepository specialistsRepository,
            TransactionsRepository transactionsRepository) {

        this.accountsRepository = accountsRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.specialistsRepository = specialistsRepository;
        this.transactionsRepository = transactionsRepository;
    }

    public List<EnterpriseAccount> getAll() {
        return accountsRepository.findAll();
    }

    public List<EnterpriseAccount> getAllByUser(BankingUserDetails userDetails) {
        checkAuthority(userDetails);

        return accountsRepository.findAllBySpecialistId(userDetails.getId());
    }

    public List<EnterpriseAccount> getAllByEnterpriseId(Long enterpriseId) {
        return accountsRepository.findAllByEnterpriseId(enterpriseId);
    }

    public EnterpriseAccount getById(Long id) {
        return accountsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + id + "' not found."));
    }

    public Long create(BankingUserDetails userDetails, Long bankId) {
        checkAuthority(userDetails);

        var specialistId = userDetails.getId();

        var specialist = specialistsRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist with id '" + specialistId + "' not found"));

        var enterpriseId = specialist.getEnterpriseId();
        var account = EnterpriseAccount.create(enterpriseId, specialistId, bankId);

        return accountsRepository.create(account);
    }

    public boolean validateOwner(Long accountId, BankingUserDetails userDetails) {
        var account = accountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found."));

        var userId = userDetails.getId();
        return account.isOwner(userId);
    }

    private void checkAuthority(BankingUserDetails userDetails) throws BadRequestException {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("SPECIALIST"))) {
            throw new BadRequestException("User is not a enterprise specialist");
        }
    }

}
