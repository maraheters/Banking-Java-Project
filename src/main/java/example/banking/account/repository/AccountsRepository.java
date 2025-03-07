package example.banking.account.repository;

import example.banking.account.entity.Account;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository {

    @Transactional
    Long create(Account account);

    @Transactional
    void update(Account account);

    Optional<Account> findById(Long id);

    List<Account> findAll();

    List<Account> findByHolderId(Long holderId);
    
    List<Account> findByUserId(Long holderId);
}
