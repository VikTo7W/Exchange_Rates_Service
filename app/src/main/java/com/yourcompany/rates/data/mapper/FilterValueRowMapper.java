package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.model.FilterValue;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilterValueRowMapper
    implements RowMapper<FilterValue> {

    @Override
    public FilterValue mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new FilterValue(
            rs.getInt("id"),
            rs.getInt("filter_id"),
            rs.getObject("currency_id", Integer.class),
            rs.getString("name"),
            rs.getString("value")
        );
    }
}
