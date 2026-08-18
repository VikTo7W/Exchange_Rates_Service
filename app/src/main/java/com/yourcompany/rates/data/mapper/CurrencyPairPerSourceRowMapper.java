package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.enums.JsonValueType;
import com.yourcompany.rates.data.model.CurrencyPairPerSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CurrencyPairPerSourceRowMapper
    implements RowMapper<CurrencyPairPerSource> {

    @Override
    public CurrencyPairPerSource mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new CurrencyPairPerSource(
            rs.getInt("pair_id"),
            rs.getInt("source_id"),
            rs.getString("from_currency_value"),
            JsonValueType.valueOf(rs.getString("from_currency_value_type")),
            rs.getString("to_currency_value"),
            JsonValueType.valueOf(rs.getString("to_currency_value_type"))
        );
    }
}
