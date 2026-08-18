package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.enums.BroadcastingStatus;
import com.yourcompany.rates.data.enums.OrderType;
import com.yourcompany.rates.data.model.RateBroadcastingSetting;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RateBroadcastingSettingRowMapper
    implements RowMapper<RateBroadcastingSetting> {

    @Override
    public RateBroadcastingSetting mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new RateBroadcastingSetting(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("source_id"),
            rs.getInt("calculation_rule_id"),
            rs.getInt("currency_pair_id"),
            OrderType.valueOf(rs.getString("orders_type")),
            rs.getInt("orders_window_start"),
            rs.getInt("orders_window_end"),
            rs.getInt("broadcasting_timeout"),
            BroadcastingStatus.valueOf(rs.getString("broadcasting_status"))
        );
    }
}
