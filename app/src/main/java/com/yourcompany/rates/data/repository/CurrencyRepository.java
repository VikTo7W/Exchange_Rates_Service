package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.enums.CurrencyType;
import com.yourcompany.rates.data.mapper.CurrencyRowMapper;
import com.yourcompany.rates.data.model.Currency;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CurrencyRepository {

    protected final JdbcTemplate jdbc;
    protected final CurrencyRowMapper mapper;

    public CurrencyRepository(
        JdbcTemplate jdbc,
        CurrencyRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public Optional<Currency> findById(int id) {
        String sql =
            """
            SELECT id, name, ticker, type, symbol, rate
            FROM "Currencies"
            WHERE id = ?
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }

    public List<Currency> findAll() {
        String sql =
            """
            SELECT id, name, ticker, type, symbol, rate
            FROM "Currencies"
            """;
        return jdbc.query(sql, mapper);
    }

    public List<Currency> findByType(CurrencyType type) {
        String sql =
            """
            SELECT id, name, ticker, type, symbol, rate
            FROM "Currencies"
            WHERE type = ?::CURRENCY_TYPE
            """;
        return jdbc.query(sql, mapper, type.name());
    }

    public Currency insert(Currency row) {
        String sql =
            """
            INSERT INTO "Currencies" (name, ticker, type, symbol, rate)
            VALUES (?, ?, ?::CURRENCY_TYPE, ?, ?)
            RETURNING id, name, ticker, type, symbol, rate
            """;
        return jdbc.queryForObject(
            sql, mapper,
            row.name(),
            row.ticker(),
            row.type().name(),
            row.symbol(),
            row.rate()
        );
    }

    public Optional<Currency> updateRate(int id, int rate) {
        String sql =
            """
            UPDATE "Currencies"
            SET rate = ?
            WHERE id = ?
            RETURNING id, name, ticker, type, symbol, rate
            """;
        return jdbc.query(sql, mapper, rate, id).stream().findFirst();
    }

    public Optional<Currency> deleteById(int id) {
        String sql =
            """
            DELETE FROM "Currencies"
            WHERE id = ?
            RETURNING id, name, ticker, type, symbol, rate
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }
}
