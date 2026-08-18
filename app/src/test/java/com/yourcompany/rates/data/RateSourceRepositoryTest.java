package com.yourcompany.rates.data;

import com.yourcompany.rates.PostgresTestContainer;
import com.yourcompany.rates.data.enums.*;
import com.yourcompany.rates.data.model.*;
import com.yourcompany.rates.data.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class RateSourceRepositoryTest extends PostgresTestContainer {

    @Autowired
    private RateSourceRepository repository;

    @Autowired
    private CurrencyPairPerSourceRepository curPairPerSourceRepository;

    @Autowired
    private CurrencyPairRepository curPairRepository;

    @Autowired
    private CurrencyRepository curRepository;

    @Autowired
    private FilterValueRepository filtValueRepository;

    @Autowired
    private FilterRepository filtRepository;

    @Autowired
    private SettingCalculationRuleRepository calcRuleRepository;

    @Autowired
    private RateBroadcastingSettingRepository settingsRepository;

    @Test
    public void insert_test() {
        assertThatCode(() -> repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP))).doesNotThrowAnyException();
        assertThatThrownBy(() -> repository.insert(new RateSource(0, "Rapira", "https://rapira.com", RequestType.GET, PayloadType.JSON,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP))).isInstanceOf(Throwable.class);
    }

    @Test
    public void findById_test() {
        repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));

        Optional<RateSource> selectedExists = repository.findById(1);
        Optional<RateSource> selectedDoesntExist = repository.findById(2);

        assertThat(selectedExists).isPresent();
        assertThat(selectedDoesntExist).isNotPresent();
    }

    @Test
    public void findAll_test() {
        repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        repository.insert(new RateSource(0, "Binance", "https://binance.com", RequestType.POST, PayloadType.JSON,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));

        List<RateSource> selected = repository.findAll();

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
    }

    @Test
    public void findByStatus_test() {
        repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        repository.insert(new RateSource(0, "Binance", "https://binance.com", RequestType.POST, PayloadType.JSON,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.DOWN));

        List<RateSource> selected = repository.findByStatus(SourceStatus.UP);

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(1);
        assertThat(selected.get(0).status()).isEqualTo(SourceStatus.UP);
    }

    @Test
    public void updateStatus_test() {
        curRepository.insert(new Currency(0, "Crypto dollar", "USDT", CurrencyType.DIGITAL, "USD₮", 100));
        curRepository.insert(new Currency(0, "Euro", "EUR", CurrencyType.FIAT, "€", 100));
        curPairRepository.insert(new CurrencyPair(0, 1, 2));
        calcRuleRepository.insert(new SettingCalculationRule(0, "Main", new BigDecimal("0.0155"), RoundingType.NEAREST, StatisticalOperation.MEAN));
        repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        settingsRepository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));

        Optional<RateSource> updated = repository.updateStatus(1, SourceStatus.DOWN);
        Optional<RateBroadcastingSetting> selectedSetting = settingsRepository.findById(1);

        assertThat(updated).isPresent();
        assertThat(updated.get().status()).isEqualTo(SourceStatus.DOWN);
        assertThat(selectedSetting.get().broadcastingStatus()).isEqualTo(BroadcastingStatus.INACTIVE);
    }

    @Test
    public void updatePayload() {
        repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,null, "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));

        Optional<RateSource> updated = repository.updatePayload(1, "payload");

        assertThat(updated).isPresent();
        assertThat(updated.get().rateEndpointPayload()).isEqualTo("payload");
    }

    @Test
    public void updateHeaders_test() {
        repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", null, 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));

        Optional<RateSource> updated = repository.updateHeaders(1, "{\"Header\": \"header\"}");

        assertThat(updated).isPresent();
        assertThat(updated.get().rateEndpointHeaders()).isEqualTo("{\"Header\": \"header\"}");
    }

    @Test
    public void updateRateKey_test() {
        repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, null, "class=\"\"", "keyPath", SourceStatus.UP));

        Optional<RateSource> updated = repository.updateRateKeyPath(1, "keyPath");

        assertThat(updated).isPresent();
        assertThat(updated.get().rateKeyPath()).isEqualTo("keyPath");
    }

    @Test
    public void updateOrdersArrayKey_test() {
        repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));

        Optional<RateSource> updated = repository.updateBuyOrdersArrayKeyPath(1, "id=\"\"");

        assertThat(updated).isPresent();
        assertThat(updated.get().buyOrdersArrayKeyPath()).isEqualTo("id=\"\"");
    }

    @Test
    public void deleteById_test() {
        curRepository.insert(new Currency(0, "Crypto dollar", "USDT", CurrencyType.DIGITAL, "USD₮", 100));
        curRepository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 100));
        curPairRepository.insert(new CurrencyPair(0, 1, 2));
        repository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        curPairPerSourceRepository.insert(new CurrencyPairPerSource(1, 1, "1", JsonValueType.NUMBER, "0", JsonValueType.NUMBER));
        filtRepository.insert(new Filter(0, 1, "payment", "Payment Method", JsonValueType.OBJECT));
        filtValueRepository.insert(new FilterValue(0, 1, null, "Cash in person", "Cash in person"));

        Optional<RateSource> deleted = repository.deleteById(1);
        List<CurrencyPairPerSource> selectedPairs = curPairPerSourceRepository.findBySourceId(1);
        List<Filter> selectedFilters = filtRepository.findBySourceId(1);
        Optional<FilterValue> selectedFilterValues = filtValueRepository.findById(1);
        List<RateSource> selected = repository.findAll();

        assertThat(deleted).isPresent();
        assertThat(selected).isEmpty();
        assertThat(selectedPairs).isEmpty();
        assertThat(selectedFilters).isEmpty();
        assertThat(selectedFilterValues).isNotPresent();
    }
}
