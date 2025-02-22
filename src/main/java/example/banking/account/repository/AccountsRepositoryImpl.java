package example.banking.account.repository;

import example.banking.account.dto.AccountDto;
import example.banking.account.entity.Account;
import example.banking.account.rowMapper.AccountRowMapper;
import example.banking.contracts.AbstractRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountsRepositoryImpl extends AbstractRepository<Account, AccountDto> implements AccountsRepository {

    public AccountsRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = new AccountRowMapper();
    }

    @Override
    public Optional<Account> findById(Long id) {
        String sql = getFindByIdSql();

        var parameterSource = new MapSqlParameterSource("id", id);

        try {
            List<Account> accounts = template.query(sql, parameterSource, (rs, rowNum) -> {
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
                return Account.fromDto(account);
            });

            return accounts.isEmpty() ? Optional.empty() : Optional.of(accounts.getFirst());

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

        return template.query(sql, parameterSource, mapper).stream()
                .map(Account::fromDto)
                .toList();
    }

    @Override
    protected String getCreateSql() {
        return "INSERT INTO account (iban, status, type, balance, date_created, holder_id) " +
                "VALUES (:iban, :status, :type, :balance, :date_created, :holder_id) " +
                "RETURNING id";
    }

    @Override
    protected String getUpdateSql() {
        return """
                    UPDATE account SET
                    iban = :iban,
                    status = :status,
                    type = :type,
                    balance = :balance,
                    date_created = :date_created,
                    holder_id = :holder_id
                    WHERE id = :id
                """;
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM account a";
    }

    @Override
    protected String getFindByIdSql() {
        return """
                    SELECT a.*, d.id AS deposit_id
                    FROM account a
                    LEFT JOIN deposit d ON a.id = d.account_id
                    WHERE a.id = :id
                """;
    }

    @Override
    protected String getRemoveSql() {
        return null;
    }

    @Override
    protected Account fromDto(AccountDto dto) {
        return Account.fromDto(dto);
    }


    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Account account) {
        var accountDto = account.toDto();
        var map = new MapSqlParameterSource();
        map.addValue("iban", accountDto.getIBAN());
        map.addValue("status", accountDto.getStatus().toString());
        map.addValue("type", accountDto.getType().toString());
        map.addValue("balance", accountDto.getBalance());
        map.addValue("date_created", accountDto.getDateCreated());
        map.addValue("holder_id", accountDto.getHolderId());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Account account) {
        var map = getMapSqlParameterSource(account);
        map.addValue("id", account.getId());
        return map;
    }
}
