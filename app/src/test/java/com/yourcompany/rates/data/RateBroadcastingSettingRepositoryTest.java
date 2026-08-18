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

public class RateBroadcastingSettingRepositoryTest extends PostgresTestContainer {

    @Autowired
    private RateBroadcastingSettingRepository repository;

    @Autowired
    private RateSourceRepository sourceRepository;

    @Autowired
    private SettingCalculationRuleRepository calcRuleRepository;

    @Autowired
    private CurrencyPairRepository curPairRepository;

    @Autowired
    private CurrencyRepository curRepository;

    @Test
    public void insert_test() {
        prepareDbEnvironment();

        assertThatThrownBy(() -> repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 6, 3, 1000, BroadcastingStatus.ACTIVE))).isInstanceOf(Throwable.class);
        assertThatCode(() -> repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 3, 1000, BroadcastingStatus.ACTIVE))).doesNotThrowAnyException();
        assertThatThrownBy(() -> repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 3, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE))).isInstanceOf(Throwable.class);
        assertThatThrownBy(() -> repository.insert(new RateBroadcastingSetting(0, "Secondary USDT-EUR", 1, 1, 1, OrderType.BUY, 11, 15, 1000, BroadcastingStatus.ACTIVE))).isInstanceOf(Throwable.class);

    }

    @Test
    public void findById_test() {
        prepareDbEnvironment();
        repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));
        repository.insert(new RateBroadcastingSetting(0, "Backup USDT-EUR", 2, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));

        Optional<RateBroadcastingSetting> selected = repository.findById(1);

        assertThat(selected).isPresent();
        assertThat(selected.get().id()).isEqualTo(1);
    }

    @Test
    public void findAll_test() {
        prepareDbEnvironment();
        repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));
        repository.insert(new RateBroadcastingSetting(0, "Backup USDT-EUR", 2, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));

        List<RateBroadcastingSetting> selected = repository.findAll();

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
    }

    @Test
    public void findByStatus() {
        prepareDbEnvironment();
        repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));
        repository.insert(new RateBroadcastingSetting(0, "Backup USDT-EUR", 2, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.INACTIVE));
        repository.insert(new RateBroadcastingSetting(0, "Main Privileged USDT-EUR", 1, 2, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));
        repository.insert(new RateBroadcastingSetting(0, "Backup Privileged USDT-EUR", 2, 2, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.INACTIVE));

        List<RateBroadcastingSetting> selected = repository.findByStatus(BroadcastingStatus.ACTIVE);

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
        assertThat(selected.get(0).broadcastingStatus()).isEqualTo(BroadcastingStatus.ACTIVE);
        assertThat(selected.get(1).broadcastingStatus()).isEqualTo(BroadcastingStatus.ACTIVE);
    }

    @Test
    public void updateStatus_test() {
        prepareDbEnvironment();
        repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));
        sourceRepository.updateStatus(1, SourceStatus.DOWN);

        assertThatThrownBy(() -> repository.updateStatus(1, BroadcastingStatus.ACTIVE)).isInstanceOf(Throwable.class);
        assertThat(repository.findById(1).get().broadcastingStatus()).isEqualTo(BroadcastingStatus.INACTIVE);

        sourceRepository.updateStatus(1, SourceStatus.UP);
        Optional<RateBroadcastingSetting> updated = repository.updateStatus(1, BroadcastingStatus.ACTIVE);
        Optional<RateBroadcastingSetting> notUpdated = repository.updateStatus(1, BroadcastingStatus.ACTIVE);

        assertThat(updated).isPresent();
        assertThat(updated.get().broadcastingStatus()).isEqualTo(BroadcastingStatus.ACTIVE);
        assertThat(notUpdated).isNotPresent();
    }

    @Test
    public void updateTimout_test() {
        prepareDbEnvironment();
        repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));

        Optional<RateBroadcastingSetting> updated = repository.updateTimeout(1, 500);

        assertThat(updated).isPresent();
        assertThat(updated.get().broadcastingTimeout()).isEqualTo(500);
    }

    @Test
    public void updateSource_test() {
        prepareDbEnvironment();
        repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));

        Optional<RateBroadcastingSetting> updated = repository.updateSource(1, 2);

        assertThat(updated).isPresent();
        assertThat(updated.get().sourceId()).isEqualTo(2);
        assertThatThrownBy(() -> repository.updateSource(1, 3)).isInstanceOf(Throwable.class);
    }

    @Test
    public void deleteById_test() {
        prepareDbEnvironment();
        repository.insert(new RateBroadcastingSetting(0, "Main USDT-EUR", 1, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));
        repository.insert(new RateBroadcastingSetting(0, "Backup USDT-EUR", 2, 1, 1, OrderType.BUY, 3, 6, 1000, BroadcastingStatus.ACTIVE));

        Optional<RateBroadcastingSetting> deleted = repository.deleteById(1);
        List<RateBroadcastingSetting> selected = repository.findAll();

        assertThat(deleted).isPresent();
        assertThat(deleted.get().id()).isEqualTo(1);
        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(1);
    }

    private void prepareDbEnvironment() {
        curRepository.insert(new Currency(0, "Crypto dollar", "USDT", CurrencyType.DIGITAL, "USD₮", 100));
        curRepository.insert(new Currency(0, "Euro", "EUR", CurrencyType.FIAT, "€", 100));
        curPairRepository.insert(new CurrencyPair(0, 1, 2));
        sourceRepository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        sourceRepository.insert(new RateSource(0, "Binance", "https://binance.com", RequestType.POST, PayloadType.JSON,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        sourceRepository.insert(new RateSource(0, "HTX", "https://htx.com", RequestType.POST, PayloadType.JSON,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.DOWN));
        calcRuleRepository.insert(new SettingCalculationRule(0, "Main", new BigDecimal("0.0155"), RoundingType.NEAREST, StatisticalOperation.MEAN));
        calcRuleRepository.insert(new SettingCalculationRule(0, "Privileged", new BigDecimal("0.0115"), RoundingType.NEAREST, StatisticalOperation.MEAN));
    }
}
