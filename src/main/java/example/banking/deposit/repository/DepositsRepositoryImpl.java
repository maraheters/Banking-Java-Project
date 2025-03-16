package example.banking.deposit.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.deposit.dto.DepositDto;
import example.banking.deposit.entity.Deposit;
import example.banking.deposit.rowMapper.DepositRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepositsRepositoryImpl
        extends AbstractRepository<Deposit, DepositDto>
        implements DepositsRepository {

    @Autowired
    public DepositsRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    public List<Deposit> findAllByClientId(Long id) {
        String sql = """
            SELECT d.* FROM public.deposit d
            LEFT JOIN public.account a ON d.account_id = a.id
            WHERE a.holder_id = :id
        """;

        var map = new MapSqlParameterSource("id", id);

        return findAllByCriteria(sql, map);
    }

    @Override
    protected String getCreateSql() {
        return "INSERT INTO " +
                "deposit(minimum, bonus, status, date_created, length_in_months, interest_rate, account_id, number_of_bonuses, last_bonus_date ) " +
                "VALUES (:minimum, :bonus, :status, :dateCreated, :lengthInMonths, :interestRate, :accountId, :numberOfBonuses, :lastBonusDate) " +
                "RETURNING id";
    }

    @Override
    protected RowMapper<DepositDto> getRowMapper() {
        return new DepositRowMapper();
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM deposit";
    }

    @Override
    protected String getFindByIdSql() {
        return "SELECT * FROM deposit WHERE deposit.id = :id";
    }

    @Override
    protected String getUpdateSql() {
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

    @Override
    protected String getDeleteSql() {
        return null;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Deposit deposit) {
        var depositDto = deposit.toDto();
        var map = new MapSqlParameterSource();

        map.addValue("minimum",         depositDto.getMinimum());
        map.addValue("bonus",           depositDto.getBonus());
        map.addValue("status",          depositDto.getStatus().toString());
        map.addValue("dateCreated",     depositDto.getCreatedAt());
        map.addValue("lengthInMonths",  depositDto.getLengthInMonths());
        map.addValue("interestRate",    depositDto.getInterestRate());
        map.addValue("accountId",       depositDto.getAccountId());
        map.addValue("numberOfBonuses", depositDto.getNumberOfBonusesYet());
        map.addValue("lastBonusDate",   depositDto.getLastBonusDate());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Deposit deposit) {
        var map = getMapSqlParameterSource(deposit);
        map.addValue("id", deposit.getId());
        return map;
    }

    @Override
    protected Deposit fromDto(DepositDto dto) {
        return Deposit.fromDto(dto);
    }

}
