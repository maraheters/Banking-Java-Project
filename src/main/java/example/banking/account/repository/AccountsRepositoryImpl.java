package example.banking.account.repository;

import example.banking.account.dto.AccountDto;
import example.banking.account.rowMapper.AccountRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public Long create(AccountDto account) {
        String sql = "INSERT INTO account (iban, status, type, balance, date_created, holder_id) " +
                     "VALUES (:iban, :status, :type, :balance, :date_created, :holder_id) " +
                     "RETURNING id";

        var map = getMapSqlParameterSource(account);

        return template.queryForObject(sql, map, Long.class);
    }

    @Override
    public void update(AccountDto account) {
        String sql = """
            UPDATE account SET
            iban = :iban,
            status = :status,
            type = :type,
            balance = :balance,
            date_created = :date_created,
            holder_id = :holder_id
            WHERE id = :id
        """;

        var map = getMapSqlParameterSource(account);
        map.addValue("id", account.getId());

        template.update(sql, map);
    }

    @Override
    public Optional<AccountDto> findById(Long id) {
        String sql = """
            SELECT a.*, d.id AS deposit_id
            FROM account a
            LEFT JOIN deposit d ON a.id = d.account_id
            WHERE a.id = :id
        """;

        var parameterSource = new MapSqlParameterSource("id", id);

        try {
            List<AccountDto> accounts = template.query(sql, parameterSource, (rs, rowNum) -> {
                AccountDto account = mapper.mapRow(rs, rowNum);
                // Collect deposit IDs
                List<Long> depositIds = new ArrayList<>();
                do {
                    long depositId = rs.getLong("deposit_id");
                    if (depositId != 0) {
                        depositIds.add(depositId);
                    }
                } while (rs.next());
                account.setDepositIds(depositIds);
                return account;
            });

            return accounts.isEmpty() ? Optional.empty() : Optional.of(accounts.getFirst());

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AccountDto> findByHolderId(Long holderId) {
        String sql = "SELECT * FROM account WHERE holder_id = :holder_id";
        var parameterSource = new MapSqlParameterSource("holder_id", holderId);

        return template.query(sql, parameterSource, mapper);
    }

    @Override
    public List<AccountDto> findAll() {
        String sql = "SELECT * FROM account a";
        try {
            return template.query(sql, mapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for accounts", e);
        }
    }

    private MapSqlParameterSource getMapSqlParameterSource(AccountDto account) {
        var map = new MapSqlParameterSource();
        map.addValue("iban", account.getIBAN());
        map.addValue("status", account.getStatus().toString());
        map.addValue("type", account.getType().toString());
        map.addValue("balance", account.getBalance());
        map.addValue("date_created", account.getDateCreated());
        map.addValue("holder_id", account.getHolderId());

        return map;
    }
}
