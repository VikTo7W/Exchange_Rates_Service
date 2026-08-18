package com.yourcompany.rates.data;

import com.yourcompany.rates.PostgresTestContainer;
import com.yourcompany.rates.data.enums.*;
import com.yourcompany.rates.data.model.*;
import com.yourcompany.rates.data.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class FixRateRepositoryTest  extends PostgresTestContainer {

    @Autowired
    private FixRateRepository repository;

    @Autowired
    private RateBroadcastingSettingRepository settingsRepository;

    @Autowired
    private SettingCalculationRuleRepository calcRuleRepository;

    @Autowired
    private RateSourceRepository sourceRepository;

    @Autowired
    private CurrencyPairRepository curPairRepository;

    @Autowired
    private CurrencyRepository curRepository;

    @Test
    public void insert_test() {
        prepareDbEnvironment();
        assertThatCode(() -> repository.insert(new FixRate(0, 1, new BigDecimal("0.9532"), OffsetDateTime.now()))).doesNotThrowAnyException();
    }

    @Test
    public void findLatestBySettingId_test() {
        prepareDbEnvironment();
        repository.insert(new FixRate(0, 1, new BigDecimal("0.9532"), OffsetDateTime.now()));
        repository.insert(new FixRate(0, 1, new BigDecimal("0.9512"), OffsetDateTime.now()));
        repository.insert(new FixRate(0, 2, new BigDecimal("0.9432"), OffsetDateTime.now()));
        repository.insert(new FixRate(0, 2, new BigDecimal("0.9412"), OffsetDateTime.now()));

        Optional<FixRate> selected = repository.findLatestBySettingId(1);

        assertThat(selected).isPresent();
        assertThat(selected.get().id()).isEqualTo(2);
    }

    @Test
    public void findAllBySettingId_test() {
        prepareDbEnvironment();
        repository.insert(new FixRate(0, 1, new BigDecimal("0.9532"), OffsetDateTime.now()));
        repository.insert(new FixRate(0, 1, new BigDecimal("0.9512"), OffsetDateTime.now()));
        repository.insert(new FixRate(0, 2, new BigDecimal("0.9432"), OffsetDateTime.now()));
        repository.insert(new FixRate(0, 2, new BigDecimal("0.9412"), OffsetDateTime.now()));

        List<FixRate> selected = repository.findAllBySettingId(1);

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
        assertThat(selected.get(0).settingId()).isEqualTo(1);
        assertThat(selected.get(1).settingId()).isEqualTo(1);
    }

    @Test
    public void findAll_test() {
        prepareDbEnvironment();
        repository.insert(new FixRate(0, 1, new BigDecimal("0.9532"), OffsetDateTime.now()));
        repository.insert(new FixRate(0, 1, new BigDecimal("0.9512"), OffsetDateTime.now()));
        repository.insert(new FixRate(0, 2, new BigDecimal("0.9432"), OffsetDateTime.now()));
        repository.insert(new FixRate(0, 2, new BigDecimal("0.9412"), OffsetDateTime.now()));

        List<FixRate> selected = repository.findAll();

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(4);
    }

    private void prepareDbEnvironment() {
        curRepository.insert(new Currency(0, "Crypto dollar", "USDT", CurrencyType.DIGITAL, "USD₮", 100));
        curRepository.insert(new Currency(0, "Euro", "EUR", CurrencyType.FIAT, "€", 100));
        curPairRepository.insert(new CurrencyPair(0, 1, 2));
        sourceRepository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        sourceRepository.insert(new RateSource(0, "Binance", "https://binance.com", RequestType.POST, PayloadType.JSON,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        calcRuleRepository.insert(new SettingCalculationRule(0, "Main", new BigDecimal("0.0155"), RoundingType.NEAREST, StatisticalOperation.MEAN));
        settingsRepository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));
        settingsRepository.insert(new RateBroadcastingSetting(0, "Backup USDT-EUR", 2, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));
    }
}
