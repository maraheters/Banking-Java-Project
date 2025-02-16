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
public class DepositsRepositoryImpl implements DepositsRepository {

    private final NamedParameterJdbcTemplate template;
    private final DepositRowMapper mapper = new DepositRowMapper();

    public DepositsRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Long create(Deposit deposit) {
        String sql =
                "INSERT INTO " +
                "deposit(balance, status, date_created, length_in_months, interest_rate, account_id, number_of_bonuses, last_bonus ) " +
                "VALUES (:balance, :status, :dateCreated, :lengthInMonths, :interestRate, :accountId, :numberOfBonuses, :lastBonus) " +
                "RETURNING id";

        var map = new MapSqlParameterSource();
        map.addValue("balance", deposit.getMinimum());
        map.addValue("status", deposit.getStatus().toString());
        map.addValue("dateCreated", deposit.getDateCreated());
        map.addValue("lengthInMonths", deposit.getLengthInMonths());
        map.addValue("interestRate", deposit.getInterestRate());
        map.addValue("accountId", deposit.getAccountId());
        map.addValue("numberOfBonuses", deposit.getNumberOfBonusesYet());
        map.addValue("lastBonus", deposit.getLastBonusDate());

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
