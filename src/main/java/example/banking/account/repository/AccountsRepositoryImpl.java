package example.banking.account.repository;

import example.banking.account.entity.Account;
import example.banking.account.mapper.AccountRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountsRepositoryImpl implements AccountsRepository {

    private final NamedParameterJdbcTemplate template;
    private final AccountRowMapper mapper = new AccountRowMapper();

    public AccountsRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Long create(Account account) {
        String sql = "INSERT INTO account (iban, status, type, balance, date_created, holder_id) " +
                     "VALUES (:iban, :status, :type, :balance, :date_created, :holder_id) " +
                     "RETURNING id";

        var map = new MapSqlParameterSource();
        map.addValue("iban", account.getIBAN());
        map.addValue("status", account.getStatus().toString());
        map.addValue("type", account.getType().toString());
        map.addValue("balance", account.getBalance());
        map.addValue("date_created", account.getDateCreated());
        map.addValue("holder_id", account.getHolderId());

        return template.queryForObject(sql, map, Long.class);
    }

    @Override
    public Optional<Account> findById(Long id) {
        String sql = "SELECT * FROM account WHERE id = :id";
        var parameterSource = new MapSqlParameterSource("id", id);

        try {
            return Optional.ofNullable(template.queryForObject(sql, parameterSource, mapper));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Account> findByHolderId(Long holderId) {
        String sql = "SELECT * FROM account WHERE holder_id = :holder_id";
        var parameterSource = new MapSqlParameterSource("holder_id", holderId);

        return template.query(sql, parameterSource, mapper);
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT * FROM account";
        try {
            return template.query(sql, mapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for accounts", e);
        }
    }
}
