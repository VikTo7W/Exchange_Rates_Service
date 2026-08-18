package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.enums.RoundingType;
import com.yourcompany.rates.data.enums.StatisticalOperation;
import com.yourcompany.rates.data.model.SettingCalculationRule;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SettingCalculationRuleRowMapper
    implements RowMapper<SettingCalculationRule> {

    @Override
    public SettingCalculationRule mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new SettingCalculationRule(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getBigDecimal("margin_percentage"),
            RoundingType.valueOf(rs.getString("rounding_type")),
            StatisticalOperation.valueOf(rs.getString("source_stat_operation"))
        );
    }
}
