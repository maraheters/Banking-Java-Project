package example.banking.account.repository;

import example.banking.account.entity.PersonalAccount;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PersonalAccountsRepository {

    @Transactional
    Long create(PersonalAccount account);

    @Transactional
    void update(PersonalAccount account);

    Optional<PersonalAccount> findById(Long id);

    List<PersonalAccount> findAll();

    List<PersonalAccount> findByHolderId(Long holderId);
}
