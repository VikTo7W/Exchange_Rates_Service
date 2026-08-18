package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.mapper.FixRateRowMapper;
import com.yourcompany.rates.data.model.FixRate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FixRateRepository {

    protected final JdbcTemplate jdbc;
    protected final FixRateRowMapper mapper;

    public FixRateRepository(
        JdbcTemplate jdbc,
        FixRateRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public Optional<FixRate> findLatestBySettingId(int settingId) {
        String sql =
            """
            SELECT id, setting_id, rate, collected_at
            FROM "FixRates"
            WHERE setting_id = ?
            ORDER BY collected_at DESC
            LIMIT 1;
            """;
        return jdbc.query(sql, mapper, settingId).stream().findFirst();
    }

    public List<FixRate> findAllBySettingId(int settingId) {
        String sql =
            """
            SELECT id, setting_id, rate, collected_at
            FROM "FixRates"
            WHERE setting_id = ?
            ORDER BY collected_at DESC
            """;
        return jdbc.query(sql, mapper, settingId);
    }

    public List<FixRate> findAll() {
        String sql =
            """
            SELECT id, setting_id, rate, collected_at
            FROM "FixRates"
            ORDER BY collected_at DESC
            """;
        return jdbc.query(sql, mapper);
    }

    public FixRate insert(FixRate row) {
        String sql =
            """
            INSERT INTO "FixRates" (setting_id, rate, collected_at)
            VALUES (?, ?, ?)
            RETURNING id, setting_id, rate, collected_at
            """;
        return jdbc.queryForObject(
            sql, mapper,
            row.settingId(),
            row.rate(),
            row.collectedAt()
        );
    }
}
