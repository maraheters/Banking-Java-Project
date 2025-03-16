package example.banking.enterprise.repository;

import example.banking.contracts.AbstractRepository;
import example.banking.enterprise.dto.EnterpriseDto;
import example.banking.enterprise.model.Enterprise;
import example.banking.enterprise.rowMapper.EnterpriseRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EnterpriseRepositoryImpl
    extends AbstractRepository<Enterprise, EnterpriseDto>
    implements EnterpriseRepository {

    @Autowired
    public EnterpriseRepositoryImpl(NamedParameterJdbcTemplate template) {
        super(template);
    }

    @Override
    protected RowMapper<EnterpriseDto> getRowMapper() {
        return new EnterpriseRowMapper();
    }

    @Override
    protected String getFindAllSql() {
        return "SELECT * FROM public.enterprise";
    }

    @Override
    protected String getFindByIdSql() {
        return "SELECT * FROM public.enterprise WHERE id = :id";
    }

    @Override
    protected String getCreateSql() {
        return """
            INSERT INTO enterprise (type, legal_name, unp, bank_id, legal_address)
            VALUES (:type, :legal_name, :unp, :bank_id, :legal_address)
            RETURNING id
        """;
    }

    @Override
    protected String getUpdateSql() {
        return """
            UPDATE enterprise
            SET
                type = :type,
                legal_name = :legal_name,
                unp = :unp,
                bank_id = :bank_id,
                legal_address = :legal_address
            WHERE id = :id
        """;
    }

    @Override
    protected String getDeleteSql() {
        return "";
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Enterprise enterprise) {
        var map = new MapSqlParameterSource();
        var dto = enterprise.toDto();
        map
                .addValue("type", dto.getType())
                .addValue("legal_name", dto.getLegalName())
                .addValue("unp", dto.getUnp())
                .addValue("bank_id", dto.getBankId())
                .addValue("legal_address", dto.getLegalAddress());

        return map;
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSourceWithId(Enterprise enterprise) {
        var map = getMapSqlParameterSource(enterprise);
        map.addValue("id", enterprise.getId());

        return map;
    }

    @Override
    protected Enterprise fromDto(EnterpriseDto dto) {
        return Enterprise.fromDto(dto);
    }
}
