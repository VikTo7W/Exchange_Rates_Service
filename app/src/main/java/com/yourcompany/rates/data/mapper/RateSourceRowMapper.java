package com.yourcompany.rates.data.mapper;

import com.yourcompany.rates.data.enums.PayloadType;
import com.yourcompany.rates.data.enums.RequestType;
import com.yourcompany.rates.data.enums.SourceStatus;
import com.yourcompany.rates.data.model.RateSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RateSourceRowMapper
    implements RowMapper<RateSource> {

    @Override
    public RateSource mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        return new RateSource(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("rate_endpoint_url"),
            RequestType.valueOf(rs.getString("rate_endpoint_type")),
            PayloadType.valueOf(rs.getString("rate_endpoint_payload_type")),
            rs.getString("rate_endpoint_payload"),
            rs.getString("payload_currency_format"),
            rs.getString("payload_from_currency_path"),
            rs.getString("payload_to_currency_path"),
            rs.getString("payload_orders_type_key_path"),
            rs.getString("payload_buy_orders_value"),
            rs.getString("payload_sell_orders_value"),
            rs.getString("rate_endpoint_headers"),
            rs.getInt("rate_array_size"),
            rs.getString("rate_key_path"),
            rs.getString("buy_orders_array_key_path"),
            rs.getString("sell_orders_array_key_path"),
            SourceStatus.valueOf(rs.getString("status"))
        );
    }
}
