package example.banking.loan.repository;

import example.banking.loan.entity.Loan;
import example.banking.loan.rowMapper.LoanRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class LoansRepositoryImpl implements LoansRepository {

    private final NamedParameterJdbcTemplate template;
    private final LoanRowMapper mapper = new LoanRowMapper();

    @Autowired
    public LoansRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Long create(Loan loan) {
        String sql = """
            INSERT INTO loan(account_id, initial_amount, paid_amount, interest_rate, length_in_months, status, created_at, last_payment)
            VALUES (:account_id, :initial_amount, :paid_amount, :interest_rate, :length_in_months, :status, :created_at, :last_payment)
            RETURNING id;
        """;

        var map = getMapSqlParameterSource(loan);

        return template.queryForObject(sql, map, Long.class);
    }

    @Override
    public void update(Loan loan) {
        String sql = """
            UPDATE loan SET
            account_id = :account_id,
            initial_amount = :initial_amount,
            paid_amount = :paid_amount,
            interest_rate = :interest_rate,
            length_in_months = :length_in_months,
            status = :status,
            created_at = :created_at,
            last_payment = :last_payment
            WHERE id = :id
        """;

        var map = getMapSqlParameterSourceWithId(loan);

        template.update(sql, map);
    }

    @Override
    public Optional<Loan> findById(Long id) {
        String sql = "SELECT * FROM loan";
        var map = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(template.queryForObject(sql, map, mapper));
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for loan", e);
        }
    }

    @Override
    public List<Loan> findAll() {
        String sql = "SELECT * FROM loan";

        try {
            return template.query(sql, mapper);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query for loans", e);
        }
    }

    private static MapSqlParameterSource getMapSqlParameterSource(Loan loan) {
        var map = new MapSqlParameterSource();
        var l = loan.toDto();
        map.addValue("account_id", l.getAccountId());
        map.addValue("initial_amount", l.getInitialAmount());
        map.addValue("paid_amount", l.getPaidAmount());
        map.addValue("interest_rate", l.getInterestRate());
        map.addValue("length_in_months", l.getLengthInMonths());
        map.addValue("status", l.getStatus().toString());
        map.addValue("created_at", l.getCreatedAt());
        map.addValue("last_payment", l.getLastPayment());

        return map;
    }

    private static MapSqlParameterSource getMapSqlParameterSourceWithId(Loan loan) {
        var map = getMapSqlParameterSource(loan);
        map.addValue("id", loan.getId());

        return map;
    }
}
