package example.banking.security.repository;

import example.banking.security.BankingUserDetails;

import java.util.Optional;

public interface UserDetailsRepository {

    Optional<BankingUserDetails> findByEmail(String email);
}
