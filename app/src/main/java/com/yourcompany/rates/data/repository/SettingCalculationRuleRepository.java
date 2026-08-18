package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.mapper.SettingCalculationRuleRowMapper;
import com.yourcompany.rates.data.model.SettingCalculationRule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class SettingCalculationRuleRepository {

    protected final JdbcTemplate jdbc;
    protected final SettingCalculationRuleRowMapper mapper;

    public SettingCalculationRuleRepository(
        JdbcTemplate jdbc,
        SettingCalculationRuleRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public Optional<SettingCalculationRule> findById(int id) {
        String sql =
            """
            SELECT id, name, margin_percentage, rounding_type, source_stat_operation
            FROM "SettingCalculationRules"
            WHERE id = ?
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }

    public List<SettingCalculationRule> findAll() {
        String sql =
            """
            SELECT id, name, margin_percentage, rounding_type, source_stat_operation
            FROM "SettingCalculationRules"
            """;
        return jdbc.query(sql, mapper);
    }

    public SettingCalculationRule insert(SettingCalculationRule row) {
        String sql =
            """
            INSERT INTO "SettingCalculationRules"
            (name, margin_percentage, rounding_type, source_stat_operation)
            VALUES (?, ?, ?::ROUNDING_TYPE, ?::STATISTICAL_OPERATION)
            RETURNING id, name, margin_percentage, rounding_type, source_stat_operation
            """;
        return jdbc.queryForObject(
            sql, mapper,
            row.name(),
            row.marginPercentage(),
            row.roundingType().name(),
            row.sourceStatOperation().name()
        );
    }

    public Optional<SettingCalculationRule> updateMargin(int id, BigDecimal marginPercentage) {
        String sql =
            """
            UPDATE "SettingCalculationRules"
            SET margin_percentage = ?
            WHERE id = ?
            RETURNING id, name, margin_percentage, rounding_type, source_stat_operation
            """;

        return jdbc.query(sql, mapper, marginPercentage, id).stream().findFirst();
    }

    public Optional<SettingCalculationRule> deleteById(int id) {
        String sql =
            """
            DELETE FROM "SettingCalculationRules"
            WHERE id = ?
            RETURNING id, name, margin_percentage, rounding_type, source_stat_operation
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }
}
