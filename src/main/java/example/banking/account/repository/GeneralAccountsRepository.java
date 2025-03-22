package example.banking.account.repository;

import example.banking.account.entity.Account;

import java.util.List;
import java.util.Optional;

public interface GeneralAccountsRepository {

    Optional<Account> findById(Long id);

    List<Account> findAllByUserId(Long id);

    void update(Account account);
}
