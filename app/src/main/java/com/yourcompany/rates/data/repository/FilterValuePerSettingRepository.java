package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.mapper.FilterValuePerSettingRowMapper;
import com.yourcompany.rates.data.model.FilterValuePerSetting;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FilterValuePerSettingRepository {

    protected final JdbcTemplate jdbc;
    protected final FilterValuePerSettingRowMapper mapper;

    public FilterValuePerSettingRepository(
        JdbcTemplate jdbc,
        FilterValuePerSettingRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public List<FilterValuePerSetting> findBySettingId(int settingId) {
        String sql =
            """
            SELECT setting_id, filter_value_id
            FROM "FilterValuesPerSetting"
            WHERE setting_id = ?
            """;
        return jdbc.query(sql, mapper, settingId);
    }

    public FilterValuePerSetting insert(FilterValuePerSetting row) {
        String sql =
            """
            INSERT INTO "FilterValuesPerSetting" (setting_id, filter_value_id)
            VALUES (?, ?)
            RETURNING setting_id, filter_value_id
            """;
        return jdbc.queryForObject(sql, mapper, row.settingId(), row.filterValueId());
    }

    public Optional<FilterValuePerSetting> delete(int settingId, int filterValueId) {
        String sql =
            """
            DELETE FROM "FilterValuesPerSetting"
            WHERE setting_id = ? AND filter_value_id = ?
            RETURNING setting_id, filter_value_id
            """;
        return jdbc.query(sql, mapper, settingId, filterValueId).stream().findFirst();
    }
}
