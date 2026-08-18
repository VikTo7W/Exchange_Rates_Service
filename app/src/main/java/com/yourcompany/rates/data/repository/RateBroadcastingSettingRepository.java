package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.enums.BroadcastingStatus;
import com.yourcompany.rates.data.mapper.RateBroadcastingSettingRowMapper;
import com.yourcompany.rates.data.model.RateBroadcastingSetting;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RateBroadcastingSettingRepository {

    protected final JdbcTemplate jdbc;
    protected final RateBroadcastingSettingRowMapper mapper;

    public RateBroadcastingSettingRepository(
        JdbcTemplate jdbc,
        RateBroadcastingSettingRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public Optional<RateBroadcastingSetting> findById(int id) {
        String sql =
            """
            SELECT id, name, source_id, calculation_rule_id,
                   currency_pair_id, orders_type,
                   orders_window_start, orders_window_end,
                   broadcasting_timeout, broadcasting_status
            FROM "RateBroadcastingSettings"
            WHERE id = ?
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }

    public List<RateBroadcastingSetting> findAll() {
        String sql =
            """
            SELECT id, name, source_id, calculation_rule_id,
                   currency_pair_id, orders_type,
                   orders_window_start, orders_window_end,
                   broadcasting_timeout, broadcasting_status
            FROM "RateBroadcastingSettings"
            """;
        return jdbc.query(sql, mapper);
    }

    public List<RateBroadcastingSetting> findByStatus(BroadcastingStatus status) {
        String sql =
            """
            SELECT id, name, source_id, calculation_rule_id,
                   currency_pair_id, orders_type,
                   orders_window_start, orders_window_end,
                   broadcasting_timeout, broadcasting_status
            FROM "RateBroadcastingSettings"
            WHERE broadcasting_status = ?::BROADCASTING_STATUS
            """;
        return jdbc.query(sql, mapper, status.name());
    }

    public RateBroadcastingSetting insert(RateBroadcastingSetting row) {
        String sql =
            """
            INSERT INTO "RateBroadcastingSettings"
            (name, source_id, calculation_rule_id, currency_pair_id,
             orders_type, orders_window_start, orders_window_end,
             broadcasting_timeout, broadcasting_status)
            VALUES (?, ?, ?, ?, ?::ORDER_TYPE, ?, ?, ?, 'INACTIVE'::BROADCASTING_STATUS)
            RETURNING id, name, source_id, calculation_rule_id,
                      currency_pair_id, orders_type,
                      orders_window_start, orders_window_end,
                      broadcasting_timeout, broadcasting_status
            """;
        return jdbc.queryForObject(
            sql, mapper,
            row.name(),
            row.sourceId(),
            row.calculationRuleId(),
            row.currencyPairId(),
            row.ordersType().name(),
            row.ordersWindowStart(),
            row.ordersWindowEnd(),
            row.broadcastingTimeout()
        );
    }

    public Optional<RateBroadcastingSetting> updateStatus(int id, BroadcastingStatus status) {
        String sql =
            """
            UPDATE "RateBroadcastingSettings"
            SET broadcasting_status = ?::BROADCASTING_STATUS
            WHERE id = ? AND broadcasting_status IS DISTINCT FROM ?::BROADCASTING_STATUS
            RETURNING id, name, source_id, calculation_rule_id,
                      currency_pair_id, orders_type,
                      orders_window_start, orders_window_end,
                      broadcasting_timeout, broadcasting_status
            """;
        return jdbc.query(sql, mapper, status.name(), id, status.name()).stream().findFirst();
    }

    public Optional<RateBroadcastingSetting> updateTimeout(int id, int timeout) {
        String sql =
            """
            UPDATE "RateBroadcastingSettings"
            SET broadcasting_timeout = ?
            WHERE id = ?
            RETURNING id, name, source_id, calculation_rule_id,
                      currency_pair_id, orders_type,
                      orders_window_start, orders_window_end,
                      broadcasting_timeout, broadcasting_status
            """;
        return jdbc.query(sql, mapper, timeout, id).stream().findFirst();
    }

    public Optional<RateBroadcastingSetting> updateSource(int id, int sourceId) {
        String sql =
            """
            UPDATE "RateBroadcastingSettings"
            SET source_id = ?
            WHERE id = ?
            RETURNING id, name, source_id, calculation_rule_id,
                      currency_pair_id, orders_type,
                      orders_window_start, orders_window_end,
                      broadcasting_timeout, broadcasting_status
            """;
        return jdbc.query(sql, mapper, sourceId, id).stream().findFirst();
    }

    public Optional<RateBroadcastingSetting> deleteById(int id) {
        String sql =
            """
            DELETE FROM "RateBroadcastingSettings"
            WHERE id = ?
            RETURNING id, name, source_id, calculation_rule_id,
                      currency_pair_id, orders_type,
                      orders_window_start, orders_window_end,
                      broadcasting_timeout, broadcasting_status
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }
}
