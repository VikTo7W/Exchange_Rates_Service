package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.model.CurrencyPair;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CurrencyPairRowMapper
    implements RowMapper<CurrencyPair> {

    @Override
    public CurrencyPair mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new CurrencyPair(
            rs.getInt("id"),
            rs.getInt("from_currency_id"),
            rs.getInt("to_currency_id")
        );
    }
}
