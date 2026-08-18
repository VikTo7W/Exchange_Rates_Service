package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.enums.CurrencyType;
import com.yourcompany.rates.data.model.Currency;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CurrencyRowMapper
    implements RowMapper<Currency> {

    @Override
    public Currency mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new Currency(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("ticker"),
            CurrencyType.valueOf(rs.getString("type")),
            rs.getString("symbol"),
            rs.getInt("rate")
        );
    }
}
