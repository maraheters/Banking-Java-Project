package example.banking.deposit.repository;

import example.banking.deposit.dto.DepositDto;
import example.banking.deposit.rowMapper.DepositRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public List<DepositDto> findAll() {
        String sql = "SELECT * FROM deposit";
        try {
            return template.query(sql, mapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for deposits", e);
        }
    }

    @Override
    public Optional<DepositDto> findById(long id) {
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
    public Long create(DepositDto deposit) {
        String sql =
                "INSERT INTO " +
                "deposit(minimum, bonus, status, date_created, length_in_months, interest_rate, account_id, number_of_bonuses, last_bonus_date ) " +
                "VALUES (:minimum, :bonus, :status, :dateCreated, :lengthInMonths, :interestRate, :accountId, :numberOfBonuses, :lastBonusDate) " +
                "RETURNING id";

        var map = getMapSqlParameterSource(deposit);

        return template.queryForObject(sql, map, Long.class);
    }

    @Override
    public void update(DepositDto deposit) {
        String sql = getUpdateSql();

        var map = getMapSqlParameterSource(deposit);
        map.addValue("id", deposit.getId());

        template.update(sql, map);
    }

    @Override
    public void batchUpdate(List<DepositDto> deposits) {
        String sql = getUpdateSql();

        List<MapSqlParameterSource> batchValues = new ArrayList<>();
        for (DepositDto deposit : deposits) {
            var map = getMapSqlParameterSource(deposit);
            map.addValue("id", deposit.getId());
            batchValues.add(map);
        }

        template.batchUpdate(sql, batchValues.toArray(new MapSqlParameterSource[0]));
    }

    private String getUpdateSql() {
        return "UPDATE deposit SET " +
                "minimum = :minimum, " +
                "bonus = :bonus, " +
                "status = :status, " +
                "date_created = :dateCreated, " +
                "length_in_months = :lengthInMonths, " +
                "interest_rate = :interestRate, " +
                "account_id = :accountId, " +
                "number_of_bonuses = :numberOfBonuses, " +
                "last_bonus_date = :lastBonusDate " +
                "WHERE id = :id";
    }

    private MapSqlParameterSource getMapSqlParameterSource(DepositDto deposit) {
        var map = new MapSqlParameterSource();

        map.addValue("minimum",         deposit.getMinimum());
        map.addValue("bonus",           deposit.getBonus());
        map.addValue("status",          deposit.getStatus().toString());
        map.addValue("dateCreated",     deposit.getDateCreated());
        map.addValue("lengthInMonths",  deposit.getLengthInMonths());
        map.addValue("interestRate",    deposit.getInterestRate());
        map.addValue("accountId",       deposit.getAccountId());
        map.addValue("numberOfBonuses", deposit.getNumberOfBonusesYet());
        map.addValue("lastBonusDate",   deposit.getLastBonusDate());

        return map;
    }

}
