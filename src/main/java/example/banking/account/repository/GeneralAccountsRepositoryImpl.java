package example.banking.account.repository;

import example.banking.account.dto.AccountDto;
import example.banking.account.entity.Account;
import example.banking.account.rowMapper.GeneralAccountRowMapper;
import example.banking.contracts.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GeneralAccountsRepositoryImpl
    extends AbstractRepository<Account, AccountDto>
    implements GeneralAccountsRepository {

    @Autowired
    public GeneralAccountsRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    protected RowMapper<AccountDto> getRowMapper() {
        return new GeneralAccountRowMapper();
    }

    @Override
    public List<Account> findAllByUserId(Long id) {
        var sql = """
            SELECT a.* FROM account a
            LEFT JOIN public.personal_account pa ON a.id = pa.id
            LEFT JOIN public.salary_account sa ON a.id = sa.id
            LEFT JOIN public.enterprise_account ea ON a.id = ea.id
        
            LEFT JOIN public.client c ON c.id = pa.holder_id OR sa.holder_id = c.id
            LEFT JOIN public.specialist s ON s.id = ea.specialist_id
        
            LEFT JOIN public.user u ON c.id = u.id OR u.id = s.id
        
            WHERE u.id = :id
        """;

        var map = new MapSqlParameterSource("id", id);

        return findAllByCriteria(sql, map);
    }

    @Override
    protected String getFindByIdSql() {
        return """
            SELECT * FROM account a
            WHERE a.id = :id
        """;
    }

    @Override
    protected String getUpdateSql() {
        return """
                UPDATE account SET
                    iban = :iban,
                    status = :status,
                    balance = :balance,
                    created_at = :created_at
                WHERE id = :id
        """;
    }

    @Override
    protected String getFindAllSql() {
        return "";
    }

    @Override
    protected String getCreateSql() {
        return "";
    }

    @Override
    protected String getDeleteSql() {
        return "";
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Account account) {
        var map = new MapSqlParameterSource();
        var dto = account.toGeneralDto();

        map
                .addValue("iban", dto.getIBAN())
                .addValue("status", dto.getStatus().toString())
                .addValue("balance", dto.getBalance())
                .addValue("created_at", dto.getCreatedAt())
                .addValue("bank_id", dto.getBankId());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Account account) {
        var map = getMapSqlParameterSource(account);

        map.addValue("id", account.getId());

        return map;
    }

    @Override
    protected Account fromDto(AccountDto dto) {
        return Account.fromGeneralDto(dto);
    }
}
