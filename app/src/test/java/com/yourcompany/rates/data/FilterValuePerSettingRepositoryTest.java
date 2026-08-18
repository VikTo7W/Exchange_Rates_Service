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

public class FilterValuePerSettingRepositoryTest extends PostgresTestContainer {

    @Autowired
    private FilterValuePerSettingRepository repository;

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

    @Autowired
    private FilterRepository filtRepository;

    @Autowired
    private FilterValueRepository filtValueRepository;

    @Test
    public void insert_test() {
        prepareDbEnvironment();

        assertThatCode(() -> repository.insert(new FilterValuePerSetting(1, 1))).doesNotThrowAnyException();
        assertThatThrownBy(() -> repository.insert(new FilterValuePerSetting(1, 1))).isInstanceOf(Throwable.class);
    }

    @Test
    public void findBySettingId_test() {
        prepareDbEnvironment();
        repository.insert(new FilterValuePerSetting(1, 1));
        repository.insert(new FilterValuePerSetting(1, 2));
        repository.insert(new FilterValuePerSetting(2, 3));
        repository.insert(new FilterValuePerSetting(2, 4));

        List<FilterValuePerSetting> selected = repository.findBySettingId(1);

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
        assertThat(selected.get(0).settingId()).isEqualTo(1);
        assertThat(selected.get(1).settingId()).isEqualTo(1);
    }

    @Test
    public void delete_test() {
        prepareDbEnvironment();
        repository.insert(new FilterValuePerSetting(1, 1));
        repository.insert(new FilterValuePerSetting(1, 2));

        Optional<FilterValuePerSetting> deleted = repository.delete(1, 1);
        List<FilterValuePerSetting> selected = repository.findBySettingId(1);

        assertThat(deleted).isPresent();
        assertThat(deleted.get().settingId()).isEqualTo(1);
        assertThat(deleted.get().filterValueId()).isEqualTo(1);
        assertThat(selected.size()).isEqualTo(1);
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
        filtRepository.insert(new Filter(0, 1, "payment", "Payment Method", JsonValueType.OBJECT));
        filtRepository.insert(new Filter(0, 2, "verified", "Verification Level", JsonValueType.OBJECT));
        filtValueRepository.insert(new FilterValue(0, 1, null, "Cash in person", "Cash in person"));
        filtValueRepository.insert(new FilterValue(0, 1, null, "Green bank","Green bank"));
        filtValueRepository.insert(new FilterValue(0, 1, null, "Paypal", "Paypal"));
        filtValueRepository.insert(new FilterValue(0, 2, null, "Verified", "Verified"));
    }
}
