package example.banking.account.service;

import example.banking.account.entity.PersonalAccount;
import example.banking.account.repository.PersonalAccountsRepository;
import example.banking.account.types.AccountStatus;
import example.banking.exception.BadRequestException;
import example.banking.exception.ResourceNotFoundException;
import example.banking.security.BankingUserDetails;
import example.banking.transaction.entity.Transaction;
import example.banking.transaction.repository.TransactionsRepository;
import example.banking.transaction.types.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PersonalAccountsService {

    private final PersonalAccountsRepository personalAccountsRepository;
    private final TransactionsRepository transactionsRepository;

    @Autowired
    public PersonalAccountsService(
            PersonalAccountsRepository personalAccountsRepository,
            TransactionsRepository transactionsRepository) {
        this.personalAccountsRepository = personalAccountsRepository;
        this.transactionsRepository = transactionsRepository;
    }

    public Long create(BankingUserDetails userDetails, Long bankId) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("BASIC"))) {
            throw new BadRequestException("User is not a client");
        }

        var account = PersonalAccount.create(userDetails.getId(), bankId);

        return personalAccountsRepository.create(account);
    }

    public PersonalAccount getById(Long id) {
        return personalAccountsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + id + "' not found."));
    }

    public List<PersonalAccount> getAll() {
        return personalAccountsRepository.findAll();
    }

    public List<PersonalAccount> getAllByUser(BankingUserDetails userDetails) {
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("BASIC"))) {
            throw new BadRequestException("User is not a client");
        }

        return personalAccountsRepository.findByHolderId(userDetails.getId());
    }

    public boolean validateOwner(Long accountId, BankingUserDetails userDetails) {
        var account = personalAccountsRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id '" + accountId + "' not found."));

        var clientId = userDetails.getId();
        return account.isOwner(clientId);
    }
}
