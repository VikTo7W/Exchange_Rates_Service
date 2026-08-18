package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.model.FilterValuePerSetting;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilterValuePerSettingRowMapper
    implements RowMapper<FilterValuePerSetting> {

    @Override
    public FilterValuePerSetting mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new FilterValuePerSetting(
            rs.getInt("setting_id"),
            rs.getInt("filter_value_id")
        );
    }
}
