package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.mapper.FilterValueRowMapper;
import com.yourcompany.rates.data.model.FilterValue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FilterValueRepository {

    protected final JdbcTemplate jdbc;
    protected final FilterValueRowMapper mapper;

    public FilterValueRepository(
        JdbcTemplate jdbc,
        FilterValueRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public Optional<FilterValue> findById(int id) {
        String sql =
            """
            SELECT id, filter_id, currency_id, name, value
            FROM "FiltersValues"
            WHERE id = ?
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }

    public List<FilterValue> findByFilterId(int filterId) {
        String sql =
            """
            SELECT id, filter_id, currency_id, name, value
            FROM "FiltersValues"
            WHERE filter_id = ?
            """;
        return jdbc.query(sql, mapper, filterId);
    }

    public List<FilterValue> findByFilterAndCurrencyId(int filterId, int currencyId) {
        String sql =
            """
            SELECT id, filter_id, currency_id, name, value
            FROM "FiltersValues"
            WHERE filter_id = ? AND (currency_id = ? OR currency_id IS NULL)
            """;
        return jdbc.query(sql, mapper, filterId, currencyId);
    }

    public FilterValue insert(FilterValue row) {
        String sql =
            """
            INSERT INTO "FiltersValues" (filter_id, currency_id, name, value)
            VALUES (?, ?, ?, ?)
            RETURNING id, filter_id, currency_id, name, value
            """;
        return jdbc.queryForObject(
            sql, mapper,
            row.filterId(),
            row.currencyId(),
            row.name(),
            row.value()
        );
    }

    public Optional<FilterValue> deleteById(int id) {
        String sql =
            """
            DELETE FROM "FiltersValues"
            WHERE id = ?
            RETURNING id, filter_id, currency_id, name, value
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }
}
