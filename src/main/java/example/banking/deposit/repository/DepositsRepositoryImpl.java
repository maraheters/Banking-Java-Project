package example.banking.deposit.repository;

import example.banking.deposit.entity.Deposit;
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
    public List<Deposit> findAll() {
        String sql = "SELECT * FROM deposit";
        try {
            return template.query(sql, mapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for deposits", e);
        }
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
    public Long create(Deposit deposit) {
        String sql =
                "INSERT INTO " +
                "deposit(minimum, bonus, status, date_created, length_in_months, interest_rate, account_id, number_of_bonuses, last_bonus_date ) " +
                "VALUES (:minimum, :bonus, :status, :dateCreated, :lengthInMonths, :interestRate, :accountId, :numberOfBonuses, :lastBonusDate) " +
                "RETURNING id";

        var map = getMapSqlParameterSource(deposit);

        return template.queryForObject(sql, map, Long.class);
    }

    @Override
    public void update(Deposit deposit) {
        String sql = getUpdateSql();

        var map = getMapSqlParameterSource(deposit);
        map.addValue("id", deposit.toDto().getId());

        template.update(sql, map);
    }

    @Override
    public void batchUpdate(List<Deposit> deposits) {
        String sql = getUpdateSql();

        List<MapSqlParameterSource> batchValues = new ArrayList<>();
        for (Deposit deposit : deposits) {
            var map = getMapSqlParameterSource(deposit);
            map.addValue("id", deposit.toDto().getId());
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

    private MapSqlParameterSource getMapSqlParameterSource(Deposit deposit) {
        var depositDto = deposit.toDto();
        var map = new MapSqlParameterSource();

        map.addValue("minimum",         depositDto.getMinimum());
        map.addValue("bonus",           depositDto.getBonus());
        map.addValue("status",          depositDto.getStatus().toString());
        map.addValue("dateCreated",     depositDto.getDateCreated());
        map.addValue("lengthInMonths",  depositDto.getLengthInMonths());
        map.addValue("interestRate",    depositDto.getInterestRate());
        map.addValue("accountId",       depositDto.getAccountId());
        map.addValue("numberOfBonuses", depositDto.getNumberOfBonusesYet());
        map.addValue("lastBonusDate",   depositDto.getLastBonusDate());

        return map;
    }

}
