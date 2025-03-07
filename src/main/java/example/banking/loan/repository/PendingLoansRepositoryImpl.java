package example.banking.loan.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.loan.dto.PendingLoanDto;
import example.banking.loan.entity.PendingLoan;
import example.banking.loan.rowMapper.PendingLoanRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PendingLoansRepositoryImpl
    extends AbstractRepository<PendingLoan, PendingLoanDto>
    implements PendingLoansRepository {

    @Autowired
    public PendingLoansRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = new PendingLoanRowMapper();
    }

    @Override
    public List<PendingLoan> findAllByUserId(Long id) {
        String sql = """
            SELECT pl.*
            FROM public.pending_loan pl
            JOIN account a ON pl.account_id = a.id
            JOIN client c ON a.holder_id = c.id
            WHERE c.user_id = :user_id;
        """ ;

        var map = new MapSqlParameterSource("user_id", id);

        return findAllByCriteria(sql, map);
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM public.pending_loan";
    }

    @Override
    protected String getFindByIdSql() {
        return "SELECT * FROM public.pending_loan WHERE id = :id";
    }

    @Override
    protected String getCreateSql() {
        return """
                INSERT INTO public.pending_loan
                    (account_id, principal_amount, interest_rate, length_in_months, requested_at, status)
                VALUES
                    (:account_id, :principal_amount, :interest_rate, :length_in_months, :requested_at, :status)
                RETURNING id""";
    }

    @Override
    protected String getUpdateSql() {
        return """
                UPDATE public.pending_loan
                SET
                    account_id = :account_id,
                    principal_amount = :principal_amount,
                    interest_rate = :interest_rate,
                    length_in_months = :length_in_months,
                    requested_at = :requested_at,
                    status = :status
                WHERE id = :id
        """;
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM public.pending_loan WHERE id = ?";
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(PendingLoan loan) {
        var dto = loan.toDto();
        var map = new MapSqlParameterSource();
        map
                .addValue("account_id", dto.getAccountId())
                .addValue("interest_rate", dto.getInterestRate())
                .addValue("principal_amount", dto.getPrincipalAmount())
                .addValue("length_in_months", dto.getLengthInMonths())
                .addValue("status", dto.getStatus().toString())
                .addValue("requested_at", dto.getRequestedAt());
        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(PendingLoan loan) {
        var map = getMapSqlParameterSource(loan);
        map.addValue("id", loan.getId());
        return map;
    }

    @Override
    protected PendingLoan fromDto(PendingLoanDto dto) {
        return PendingLoan.fromDto(dto);
    }
}
