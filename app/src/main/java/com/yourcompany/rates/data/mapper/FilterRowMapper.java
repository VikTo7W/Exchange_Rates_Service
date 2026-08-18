package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.enums.JsonValueType;
import com.yourcompany.rates.data.model.Filter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilterRowMapper
    implements RowMapper<Filter> {

    @Override
    public Filter mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new Filter(
            rs.getInt("id"),
            rs.getInt("source_id"),
            rs.getString("key_path"),
            rs.getString("name"),
            JsonValueType.valueOf(rs.getString("value_type"))
        );
    }
}
