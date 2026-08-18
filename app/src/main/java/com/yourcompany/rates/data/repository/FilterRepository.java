package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.mapper.FilterRowMapper;
import com.yourcompany.rates.data.model.Filter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FilterRepository {

    protected final JdbcTemplate jdbc;
    protected final FilterRowMapper mapper;

    public FilterRepository(
        JdbcTemplate jdbc,
        FilterRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public List<Filter> findBySourceId(int sourceId) {
        String sql =
            """
            SELECT id, source_id, key_path, name, value_type
            FROM "Filters"
            WHERE source_id = ?
            """;
        return jdbc.query(sql, mapper, sourceId);
    }

    public Optional<Filter> findById(int id) {
        String sql =
            """
            SELECT id, source_id, key_path, name, value_type
            FROM "Filters"
            WHERE id = ?
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }

    public Filter insert(Filter row) {
        String sql =
            """
            INSERT INTO "Filters" (source_id, key_path, name, value_type)
            VALUES (?, ?, ?, ?::JSON_VALUE)
            RETURNING id, source_id, key_path, name, value_type
            """;
        return jdbc.queryForObject(
            sql, mapper,
            row.sourceId(),
            row.keyPath(),
            row.name(),
            row.valueType().name()
        );
    }

    public Optional<Filter> updateKeyPath(int id, String key) {
        String sql =
            """
            UPDATE "Filters"
            SET key_path = ?
            WHERE id = ?
            RETURNING id, source_id, key_path, name, value_type
            """;
        return jdbc.query(sql, mapper, key, id).stream().findFirst();
    }

    public Optional<Filter> deleteById(int id) {
        String sql =
            """
            DELETE FROM "Filters"
            WHERE id = ?
            RETURNING id, source_id, key_path, name, value_type
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }
}
