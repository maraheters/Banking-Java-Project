package example.banking.loan.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.loan.dto.LoanDto;
import example.banking.loan.entity.Loan;
import example.banking.loan.rowMapper.LoanRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class LoansRepositoryImpl extends AbstractRepository<Loan, LoanDto> implements LoansRepository {

    @Autowired
    public LoansRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = new LoanRowMapper();
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM loan";
    }

    @Override
    protected String getFindByIdSql() {
        return "SELECT * FROM loan WHERE id = :id";
    }

    @Override
    protected String getCreateSql() {
        return """
            INSERT INTO loan(account_id, principal_amount, paid_amount, interest_rate, length_in_months, status, created_at, last_payment)
            VALUES (:account_id, :principal_amount, :paid_amount, :interest_rate, :length_in_months, :status, :created_at, :last_payment)
            RETURNING id;
        """;
    }

    @Override
    protected String getUpdateSql() {
        return """
            UPDATE loan SET
            account_id = :account_id,
            principal_amount = :principal_amount,
            paid_amount = :paid_amount,
            interest_rate = :interest_rate,
            length_in_months = :length_in_months,
            status = :status,
            created_at = :created_at,
            last_payment = :last_payment
            WHERE id = :id
        """;
    }

    @Override
    protected String getRemoveSql() {
        return null;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Loan loan) {
        var map = new MapSqlParameterSource();
        var l = loan.toDto();
        map.addValue("account_id", l.getAccountId());
        map.addValue("principal_amount", l.getPrincipalAmount());
        map.addValue("paid_amount", l.getPaidAmount());
        map.addValue("interest_rate", l.getInterestRate());
        map.addValue("length_in_months", l.getLengthInMonths());
        map.addValue("status", l.getStatus().toString());
        map.addValue("created_at", l.getCreatedAt());
        map.addValue("last_payment", l.getLastPayment());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Loan loan) {
        var map = getMapSqlParameterSource(loan);
        map.addValue("id", loan.getId());

        return map;
    }

    @Override
    protected Loan fromDto(LoanDto dto) {
        return Loan.fromDto(dto);
    }
}
