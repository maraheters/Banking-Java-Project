package example.banking.account.repository;

import example.banking.account.dto.AccountDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository {

    @Transactional
    Long create(AccountDto account);

    @Transactional
    void update(AccountDto account);

    Optional<AccountDto> findById(Long id);

    List<AccountDto> findAll();

    List<AccountDto> findByHolderId(Long holderId);
}
