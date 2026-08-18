package com.yourcompany.rates.data.repository;

import com.yourcompany.rates.data.enums.SourceStatus;
import com.yourcompany.rates.data.mapper.RateSourceRowMapper;
import com.yourcompany.rates.data.model.RateSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RateSourceRepository {

    protected final JdbcTemplate jdbc;
    protected final RateSourceRowMapper mapper;

    public RateSourceRepository(
        JdbcTemplate jdbc,
        RateSourceRowMapper mapper
    ) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public Optional<RateSource> findById(int id) {
        String sql =
            """
            SELECT id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            FROM "RateSources"
            WHERE id = ?
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }

    public List<RateSource> findAll() {
        String sql =
            """
            SELECT id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            FROM "RateSources"
            """;
        return jdbc.query(sql, mapper);
    }

    public List<RateSource> findByStatus(SourceStatus status) {
        String sql =
            """
            SELECT id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            FROM "RateSources"
            WHERE status = ?::SOURCE_STATUS
            """;
        return jdbc.query(sql, mapper, status.name());
    }

    public Optional<RateSource> updateStatus(int id, SourceStatus status) {
        String sql =
            """
            UPDATE "RateSources"
            SET status = ?::SOURCE_STATUS
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, status.name(), id).stream().findFirst();
    }

    public Optional<RateSource> updatePayload(int id, String payload) {
        String sql =
            """
            UPDATE "RateSources"
            SET rate_endpoint_payload = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, payload, id).stream().findFirst();
    }

    public Optional<RateSource> updatePayloadCurrencyFormat(int id, String currencyFormat) {
        String sql =
            """
            UPDATE "RateSources"
            SET payload_currency_format = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, currencyFormat, id).stream().findFirst();
    }

    public Optional<RateSource> updatePayloadFromCurrencyPath(int id, String fromCurrencyPath) {
        String sql =
            """
            UPDATE "RateSources"
            SET payload_from_currency_path = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, fromCurrencyPath, id).stream().findFirst();
    }

    public Optional<RateSource> updatePayloadToCurrencyPath(int id, String toCurrencyPath) {
        String sql =
            """
            UPDATE "RateSources"
            SET payload_to_currency_path = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, toCurrencyPath, id).stream().findFirst();
    }

    public Optional<RateSource> updatePayloadOrdersTypeKeyPath(int id, String ordersTypeKeyPath) {
        String sql =
            """
            UPDATE "RateSources"
            SET payload_orders_type_key_path = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, ordersTypeKeyPath, id).stream().findFirst();
    }

    public Optional<RateSource> updatePayloadBuyOrdersValue(int id, String buyOrdersValue) {
        String sql =
            """
            UPDATE "RateSources"
            SET payload_buy_orders_value = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, buyOrdersValue, id).stream().findFirst();
    }

    public Optional<RateSource> updatePayloadSellOrdersValue(int id, String sellOrdersValue) {
        String sql =
            """
            UPDATE "RateSources"
            SET payload_sell_orders_value = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, sellOrdersValue, id).stream().findFirst();
    }

    public Optional<RateSource> updateHeaders(int id, String headers) {
        String sql =
            """
            UPDATE "RateSources"
            SET rate_endpoint_headers = ?::JSONB
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, headers, id).stream().findFirst();
    }

    public Optional<RateSource> updateRateKeyPath(int id, String rateKeyPath) {
        String sql =
            """
            UPDATE "RateSources"
            SET rate_key_path = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, rateKeyPath, id).stream().findFirst();
    }

    public Optional<RateSource> updateBuyOrdersArrayKeyPath(int id, String buyOrdersArrayKeyPath) {
        String sql =
            """
            UPDATE "RateSources"
            SET buy_orders_array_key_path = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, buyOrdersArrayKeyPath, id).stream().findFirst();
    }

    public Optional<RateSource> updateSellOrdersArrayKeyPath(int id, String sellOrdersArrayKeyPath) {
        String sql =
            """
            UPDATE "RateSources"
            SET sell_orders_array_key_path = ?
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, sellOrdersArrayKeyPath, id).stream().findFirst();
    }

    public RateSource insert(RateSource row) {
        String sql =
            """
            INSERT INTO "RateSources"
            (name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status)
            VALUES (?, ?, ?::REQUEST_TYPE, ?::PAYLOAD_TYPE, ?, ?, ?, ?, ?, ?, ?, ?::JSONB, ?, ?, ?, ?, ?::SOURCE_STATUS)
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.queryForObject(
            sql, mapper,
            row.name(),
            row.rateEndpointUrl(),
            row.rateEndpointType().name(),
            row.rateEndpointPayloadType().name(),
            row.rateEndpointPayload(),
            row.payloadCurrencyFormat(),
            row.payloadFromCurrencyPath(),
            row.payloadToCurrencyPath(),
            row.payloadOrdersTypeKeyPath(),
            row.payloadBuyOrdersValue(),
            row.payloadSellOrdersValue(),
            row.rateEndpointHeaders(),
            row.rateArraySize(),
            row.rateKeyPath(),
            row.buyOrdersArrayKeyPath(),
            row.sellOrdersArrayKeyPath(),
            row.status().name()
        );
    }

    public Optional<RateSource> deleteById(int id) {
        String sql =
            """
            DELETE FROM "RateSources"
            WHERE id = ?
            RETURNING id, name, rate_endpoint_url, rate_endpoint_type,
                   rate_endpoint_payload_type, rate_endpoint_payload, payload_currency_format,
                   payload_from_currency_path, payload_to_currency_path, payload_orders_type_key_path,
                   payload_buy_orders_value, payload_sell_orders_value, rate_endpoint_headers, rate_array_size,
                   rate_key_path, buy_orders_array_key_path, sell_orders_array_key_path, status
            """;
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }
}
