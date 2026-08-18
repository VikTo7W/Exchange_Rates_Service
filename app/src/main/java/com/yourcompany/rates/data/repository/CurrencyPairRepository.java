package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.mapper.CurrencyPairRowMapper;
import com.yourcompany.rates.data.model.CurrencyPair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CurrencyPairRepository {

    protected final JdbcTemplate jdbc;
    protected final CurrencyPairRowMapper mapper;

    public CurrencyPairRepository(
        JdbcTemplate jdbc,
        CurrencyPairRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public Optional<CurrencyPair> findById(int id) {
        String sql =
            """
            SELECT id, from_currency_id, to_currency_id
            FROM "CurrencyPairs"
            WHERE id = ?
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }

    public List<CurrencyPair> findAll() {
        String sql =
            """
            SELECT id, from_currency_id, to_currency_id
            FROM "CurrencyPairs"
            """;
        return jdbc.query(sql, mapper);
    }

    public CurrencyPair insert(CurrencyPair row) {
        String sql =
            """
            INSERT INTO "CurrencyPairs" (from_currency_id, to_currency_id)
            VALUES (?, ?)
            RETURNING id, from_currency_id, to_currency_id
            """;
        return jdbc.queryForObject(
            sql, mapper,
            row.fromCurrencyId(),
            row.toCurrencyId()
        );
    }

    public Optional<CurrencyPair> deleteById(int id) {
        String sql =
            """
            DELETE FROM "CurrencyPairs"
            WHERE id = ?
            RETURNING id, from_currency_id, to_currency_id
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }
}
