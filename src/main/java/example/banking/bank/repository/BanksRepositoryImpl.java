package example.banking.bank.repository;

import example.banking.bank.dto.BankDto;
import example.banking.bank.model.Bank;
import example.banking.bank.rowMapper.BankMapper;
import example.banking.contracts.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BanksRepositoryImpl
    extends AbstractRepository<Bank, BankDto>
    implements BanksRepository {

    @Autowired
    public BanksRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = new BankMapper();
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM public.bank";
    }

    @Override
    protected String getFindByIdSql() {
        return "SELECT * FROM public.bank WHERE id = :id";
    }

    @Override
    protected String getCreateSql() {
        return """
            INSERT INTO public.bank(name, bic, address)
            VALUES (:name, :bic, :address)
            RETURNING id
        """;
    }

    @Override
    protected String getUpdateSql() {
        return "";
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM bank WHERE id = :id";
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Bank bank) {
        var map = new MapSqlParameterSource();
        var dto = bank.toDto();
        map
                .addValue("name", dto.getName())
                .addValue("bic", dto.getBic())
                .addValue("address", dto.getAddress());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Bank bank) {
        var map = getMapSqlParameterSource(bank);
        map.addValue("id", bank.getId());

        return map;
    }

    @Override
    protected Bank fromDto(BankDto dto) {
        return Bank.fromDto(dto);
    }
}
