package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.mapper.CurrencyPairPerSourceRowMapper;
import com.yourcompany.rates.data.model.CurrencyPairPerSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CurrencyPairPerSourceRepository {

    protected final JdbcTemplate jdbc;
    protected final CurrencyPairPerSourceRowMapper mapper;

    public CurrencyPairPerSourceRepository(
        JdbcTemplate jdbc,
        CurrencyPairPerSourceRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public List<CurrencyPairPerSource> findBySourceId(int sourceId) {
        String sql =
            """
            SELECT pair_id, source_id, from_currency_value,
                      from_currency_value_type, to_currency_value, to_currency_value_type
            FROM "CurrencyPairsPerSource"
            WHERE source_id = ?
            """;
        return jdbc.query(sql, mapper, sourceId);
    }

    public Optional<CurrencyPairPerSource> findByIds(int sourceId, int pairId) {
        String sql =
            """
            SELECT pair_id, source_id, from_currency_value,
                      from_currency_value_type, to_currency_value, to_currency_value_type
            FROM "CurrencyPairsPerSource"
            WHERE source_id = ? AND pair_id = ?
            """;
        return jdbc.query(sql, mapper, sourceId, pairId).stream().findFirst();
    }

    public CurrencyPairPerSource insert(CurrencyPairPerSource row) {
        String sql =
            """
            INSERT INTO "CurrencyPairsPerSource" (pair_id, source_id, from_currency_value,
                      from_currency_value_type, to_currency_value, to_currency_value_type)
            VALUES (?, ?, ?, ?::JSON_VALUE, ?, ?::JSON_VALUE)
            RETURNING pair_id, source_id, from_currency_value,
                      from_currency_value_type, to_currency_value, to_currency_value_type
            """;
        return jdbc.queryForObject(sql, mapper,
            row.pairId(),
            row.sourceId(),
            row.fromCurrencyValue(),
            row.fromCurrencyValueType().name(),
            row.toCurrencyValue(),
            row.toCurrencyValueType().name()
        );
    }

    public Optional<CurrencyPairPerSource> delete(int pairId, int sourceId) {
        String sql =
            """
            DELETE FROM "CurrencyPairsPerSource"
            WHERE pair_id = ? AND source_id = ?
            RETURNING pair_id, source_id, from_currency_value,
                      from_currency_value_type, to_currency_value, to_currency_value_type
            """;
        return jdbc.query(sql, mapper, pairId, sourceId).stream().findFirst();
    }
}
