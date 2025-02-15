package example.banking.user.repository;

import example.banking.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {

    @Transactional
    Long create(User user);

    Optional<User> findById(long id);

    List<User> findAll();
}
