package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.model.FixRate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Component
public class FixRateRowMapper
    implements RowMapper<FixRate> {

    @Override
    public FixRate mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new FixRate(
            rs.getLong("id"),
            rs.getInt("setting_id"),
            rs.getBigDecimal("rate"),
            rs.getObject("collected_at", OffsetDateTime.class)
        );
    }
}
