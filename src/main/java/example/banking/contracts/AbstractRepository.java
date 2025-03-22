package example.banking.contracts;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<E, D> {

    protected final RowMapper<D> mapper;
    protected final NamedParameterJdbcTemplate template;

    public AbstractRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.mapper = getRowMapper();
    }

    public Long create(E entity) {
        String sql = getCreateSql();
        var map = getMapSqlParameterSource(entity);

        return template.queryForObject(sql, map, Long.class);
    }

    public List<Long> batchCreate(List<E> entities) {
        if (entities.isEmpty())
            return Collections.emptyList();

        String sql = getCreateSql();

        List<MapSqlParameterSource> batchValues = entities.stream()
                .map(this::getMapSqlParameterSource)
                .toList();

        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.batchUpdate(sql, batchValues.toArray(new MapSqlParameterSource[0]), keyHolder);

        return keyHolder.getKeyList().stream()
                .map(map -> Long.valueOf((Integer) map.get("id")))
                .toList();
    }


    public void update(E entity) {
        String sql = getUpdateSql();
        var map = getMapSqlParameterSourceWithId(entity);

        template.update(sql, map);
    }

    public void batchUpdate(List<E> entities) {
        if (entities.isEmpty())
            return;

        String sql = getUpdateSql();

        List<MapSqlParameterSource> batchValues = new ArrayList<>();
        for (E entity : entities) {
            var map = getMapSqlParameterSourceWithId(entity);
            batchValues.add(map);
        }

        template.batchUpdate(sql, batchValues.toArray(new MapSqlParameterSource[0]));
    }

    public List<E> findAll() {
        String sql = getFindAllSql();

        try {
            return template.query(sql, mapper).stream()
                    .map(this::fromDto)
                    .toList();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query: ", e);
        }
    }

    public Optional<E> findById(Long id) {
        String sql = getFindByIdSql();
        var map = new MapSqlParameterSource("id", id);

        return findByCriteria(sql, map);
    }

    public void delete(Long id) {
        String sql = getDeleteSql();
        var map = new MapSqlParameterSource("id", id);

        template.update(sql, map);
    }

    protected List<E> findAllByCriteria(String sql, MapSqlParameterSource map) {
        try {
            return template.query(sql, map, this.mapper).stream()
                    .map(this::fromDto)
                    .toList();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query: ", e);
        }
    }

    protected Optional<E> findByCriteria(String sql, MapSqlParameterSource map) {
        try {
            return Optional.of(
                    fromDto(template.queryForObject(sql, map, mapper))
            );

        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while trying to query: ", e);
        }
    }

    protected abstract RowMapper<D> getRowMapper();

    protected abstract String getFindAllSql();
    protected abstract String getFindByIdSql();
    protected abstract String getCreateSql();
    protected abstract String getUpdateSql();
    protected abstract String getDeleteSql();

    protected abstract MapSqlParameterSource getMapSqlParameterSource(E entity);
    protected abstract MapSqlParameterSource getMapSqlParameterSourceWithId(E entity);

    protected abstract E fromDto(D dto);
}
