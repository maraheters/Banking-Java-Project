package example.banking.deposit.repository;

import example.banking.deposit.entity.Deposit;
import example.banking.deposit.mapper.DepositRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DepositRepositoryImpl implements DepositRepository {

    private final NamedParameterJdbcTemplate template;
    private final DepositRowMapper mapper = new DepositRowMapper();

    public DepositRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Long create(Deposit deposit) {
        String sql = "INSERT INTO deposit(balance, status, date_created, interest_rate, account_id ) " +
                     "VALUES (:balance, :status, :dateCreated, :interestRate, :accountId) RETURNING id";

        var map = new MapSqlParameterSource();
        map.addValue("balance", deposit.getBalance());
        map.addValue("status", deposit.getStatus().toString());
        map.addValue("dateCreated", deposit.getDateCreated());
        map.addValue("interestRate", deposit.getInterestRate());
        map.addValue("accountId", deposit.getAccountId());

        return template.queryForObject(sql, map, Long.class);
    }

    @Override
    public Optional<Deposit> findById(long id) {
        String sql = "SELECT * FROM deposit WHERE deposit.id = :id";
        var parameterSource = new MapSqlParameterSource("id", id);

        try {
            return Optional.ofNullable(template.queryForObject(sql, parameterSource, mapper));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();

        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for deposit with id '" + id +"': " + e);
        }
    }

    @Override
    public List<Deposit> findAll() {
        String sql = "SELECT * FROM deposit";
        try {
            return template.query(sql, mapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for deposits", e);
        }
    }

}
