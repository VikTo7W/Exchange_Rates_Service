package com.yourcompany.rates.service;

import com.yourcompany.rates.PostgresTestContainer;
import com.yourcompany.rates.data.enums.*;
import com.yourcompany.rates.data.model.*;
import com.yourcompany.rates.service.crud.*;
import com.yourcompany.rates.service.worker.event.RatesEventPublisher;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Duration;

public class BroadcastingWorkerManagerTest extends PostgresTestContainer {

    @Autowired
    private RateBroadcastingSettingService settingService;

    @Autowired
    private RateSourceService sourceService;

    @Autowired
    private SettingCalculationRuleService calcRuleService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private FilterService filterService;

    @Autowired
    private FilterValueService filterValueService;

    @Autowired
    private FilterValuePerSettingService filterValuePerSettingService;

    @Autowired
    private CurrencyPairPerSourceService currencyPairPerSourceService;

    @Autowired
    private CurrencyPairService currencyPairService;

    @Autowired
    FixRateService fixRateService;

    @Test
    public void HybridUsdtRubRateBroadcasting_Test() {
        addCalcRuleAndCurrencyPairs();
        addHybridUdtRub();
        settingService.changeStatus(1, BroadcastingStatus.ACTIVE);
        Awaitility.await()
            .atMost(Duration.ofSeconds(10))
            .untilAsserted(() -> {
                assertThat(fixRateService.getAllPerSetting(1)).isNotEmpty();
            });
    }

    @Test
    public void RapiraUsdtRubRateBroadcasting_Test() {
        addCalcRuleAndCurrencyPairs();
        addRapiraUsdtRub();
        settingService.changeStatus(1, BroadcastingStatus.ACTIVE);
        Awaitility.await()
            .atMost(Duration.ofSeconds(10))
            .untilAsserted(() -> {
                assertThat(fixRateService.getAllPerSetting(1)).isNotEmpty();
            });
    }

    @Test
    public void HTXRateBroadcasting_Test() {
        addCalcRuleAndCurrencyPairs();
        addHtxUsdtRub();
        addSettingHTX("HTX ETH-RUB", 2, 2, "3", "11");
        addSettingHTX("HTX BTC-RUB", 3, 3, "1", "11");
        settingService.changeStatus(1, BroadcastingStatus.ACTIVE);
        settingService.changeStatus(2, BroadcastingStatus.ACTIVE);
        settingService.changeStatus(3, BroadcastingStatus.ACTIVE);
        Awaitility.await()
            .atMost(Duration.ofSeconds(10))
            .untilAsserted(() -> {
                assertThat(fixRateService.getAllPerSetting(1)).isNotEmpty();
                assertThat(fixRateService.getAllPerSetting(2)).isNotEmpty();
                assertThat(fixRateService.getAllPerSetting(3)).isNotEmpty();
            });
    }

    @Test
    public void BybitRateBroadcasting_Test() {
        addCalcRuleAndCurrencyPairs();
        addBybitUsdtRub();
        addSettingBybit("Bybit ETH-RUB", 2, 2, "ETH", "RUB");
        addSettingBybit("Bybit BTC-RUB", 3, 3, "BTC", "RUB");
        settingService.changeStatus(1, BroadcastingStatus.ACTIVE);
        settingService.changeStatus(2, BroadcastingStatus.ACTIVE);
        settingService.changeStatus(3, BroadcastingStatus.ACTIVE);
        Awaitility.await()
            .atMost(Duration.ofSeconds(10))
            .untilAsserted(() -> {
                assertThat(fixRateService.getAllPerSetting(1)).isNotEmpty();
                assertThat(fixRateService.getAllPerSetting(2)).isNotEmpty();
                assertThat(fixRateService.getAllPerSetting(3)).isNotEmpty();
            });
    }

    private void addSettingHTX(String name, int settingId, int pairId, String fromCurrencyValue, String toCurrencyValue) {
        currencyPairPerSourceService.add(new CurrencyPairPerSource(pairId, 1, fromCurrencyValue, JsonValueType.STRING, toCurrencyValue, JsonValueType.STRING));
        settingService.add(new RateBroadcastingSetting(0, name, 1, 1, pairId, OrderType.BUY, 1, 9, 1000, BroadcastingStatus.INACTIVE));

        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 2));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 6));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 10));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 12));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 14));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 15));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 17));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 18));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 19));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 20));
    }

    private void addSettingBybit(String name, int settingId, int pairId, String fromCurrencyValue, String toCurrencyValue) {
        currencyPairPerSourceService.add(new CurrencyPairPerSource(pairId, 1, fromCurrencyValue, JsonValueType.STRING, toCurrencyValue, JsonValueType.STRING));
        settingService.add(new RateBroadcastingSetting(0, name, 1, 1, pairId, OrderType.BUY, 1, 9, 1000, BroadcastingStatus.INACTIVE));

        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 1));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 4));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 6));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 8));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 12));
        filterValuePerSettingService.add(new FilterValuePerSetting(settingId, 17));
    }

    private void addCalcRuleAndCurrencyPairs() {
        calcRuleService.add(new SettingCalculationRule(0, "Main", BigDecimal.valueOf(0.0152), RoundingType.NEAREST, StatisticalOperation.MEAN));
        currencyService.add(new Currency(0, "Crypto Dollar", "USDT", CurrencyType.DIGITAL, "USD₮", 100));
        currencyService.add(new Currency(0, "Etherium", "ETH", CurrencyType.DIGITAL, "ETH", 100000000));
        currencyService.add(new Currency(0, "Bitcoin", "BTC", CurrencyType.DIGITAL, "₿", 100000000));
        currencyService.add(new Currency(0, "Ruble", "RUB", CurrencyType.FIAT, "₽", 100));
        currencyPairService.add(new CurrencyPair(0, 1, 4));
        currencyPairService.add(new CurrencyPair(0, 2, 4));
        currencyPairService.add(new CurrencyPair(0, 3, 4));
    }

    private void addHybridUdtRub() {
        sourceService.add(new RateSource(
            0,
            "Hybrid-Rate",
            "https://hybrid-rate.com/",
            RequestType.GET,
            PayloadType.NONE,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            1,
            "//*[@id=\"hybrid-rate\"]",
            null,
            null,
            SourceStatus.UP));


        currencyPairPerSourceService.add(new CurrencyPairPerSource(1, 1, null, JsonValueType.NULL, null, JsonValueType.NULL));
        settingService.add(new RateBroadcastingSetting(0, "Hybrid USDT-RUB", 1, 1, 1, OrderType.NONE, 1, 1, 1000, BroadcastingStatus.INACTIVE));
    }

    private void addRapiraUsdtRub() {
        sourceService.add(new RateSource(
            0,
            "Rapira",
            "https://api.rapira.net/market/exchange-plate-mini",
            RequestType.POST,
            PayloadType.FORM_DATA,
            null,
            "@fromCurrency/@toCurrency",
            "symbol",
            "symbol",
            null,
            null,
            null,
            null,
            24,
            "$.price",
            "$.bid.items",
            "$.ask.items",
            SourceStatus.UP));

        currencyPairPerSourceService.add(new CurrencyPairPerSource(1, 1, "USDT", JsonValueType.STRING, "RUB", JsonValueType.STRING));
        settingService.add(new RateBroadcastingSetting(0, "Rapira USDT-RUB", 1, 1, 1, OrderType.BUY, 1, 10, 1000, BroadcastingStatus.INACTIVE));
    }

    private void addHtxUsdtRub() {
        sourceService.add(new RateSource(
            0,
            "HTX",
            "https://www.htx.com/-/x/otc/v1/data/trade-market",
            RequestType.GET,
            PayloadType.URL_PARAMETERS,
            "?coinId=2&currency=11&tradeType=&currPage=1&payMethod=&acceptOrder=-1&country=&blockType=general&online=1&range=0&amount=&isThumbsUp=&isMerchant=&isTraded=&onlyTradable=&isFollowed=&makerCompleteRate=&brandLabelIds=",
            null,
            "coinId",
            "currency",
            "tradeType",
            "buy",
            "sell",
            null,
            10,
            "$.price",
            "$.data",
            "$.data",
            SourceStatus.UP));

        filterService.add(new Filter(0,1, "onlyTradable", "Available", JsonValueType.STRING));
        filterService.add(new Filter(0,1, "labelId", "Events", JsonValueType.STRING));
        filterService.add(new Filter(0,1, "makerCompleteRate", "Advertiser's 30D completion rate ≥", JsonValueType.STRING));
        filterService.add(new Filter(0,1, "isThumbsUp", "Thumbs Up Advertisers", JsonValueType.STRING));
        filterService.add(new Filter(0,1, "isMerchant", "Verified Advertisers", JsonValueType.STRING));
        filterService.add(new Filter(0,1, "isFollowed", "Followed Advertisers", JsonValueType.STRING));
        filterService.add(new Filter(0,1, "payMethod", "Payment Method", JsonValueType.STRING));

        filterValueService.add(new FilterValue(0, 1, null, "Only Tradable", "true"));
        filterValueService.add(new FilterValue(0, 1, null, "Not Only Tradable", "false"));
        filterValueService.add(new FilterValue(0, 2, null, "Partner HTX", "47"));
        filterValueService.add(new FilterValue(0, 3, null, "0%", "0"));
        filterValueService.add(new FilterValue(0, 3, null, "60%", "60"));
        filterValueService.add(new FilterValue(0, 3, null, "70%", "70"));
        filterValueService.add(new FilterValue(0, 3, null, "80%", "80"));
        filterValueService.add(new FilterValue(0, 3, null, "90%", "90"));
        filterValueService.add(new FilterValue(0, 4, null, "Thumbs Up Advertisers only", "true"));
        filterValueService.add(new FilterValue(0, 4, null, "Not Thumbs Up Advertisers only", "false"));
        filterValueService.add(new FilterValue(0, 5, null, "Verified Advertisers only", "true"));
        filterValueService.add(new FilterValue(0, 5, null, "Not Verified Advertisers only", "false"));
        filterValueService.add(new FilterValue(0, 6, null, "Followed only", "true"));
        filterValueService.add(new FilterValue(0, 6, null, "Not Followed only", "false"));
        filterValueService.add(new FilterValue(0, 7, 4, "Freedom bank", "12345"));
        filterValueService.add(new FilterValue(0, 7, 4, "Cash in Person", "169"));
        filterValueService.add(new FilterValue(0, 7, 4, "SBP - Fast Bank Transfer", "69"));
        filterValueService.add(new FilterValue(0, 7, 4, "Tinkoff", "28"));
        filterValueService.add(new FilterValue(0, 7, 4, "Raiffeisenbank", "36"));
        filterValueService.add(new FilterValue(0, 7, 4, "Sberbank", "29"));

        currencyPairPerSourceService.add(new CurrencyPairPerSource(1, 1, "2", JsonValueType.STRING, "11", JsonValueType.STRING));
        settingService.add(new RateBroadcastingSetting(0, "HTX USDT-RUB", 1, 1, 1, OrderType.BUY, 1, 9, 1000, BroadcastingStatus.INACTIVE));

        filterValuePerSettingService.add(new FilterValuePerSetting(1, 2));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 6));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 10));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 12));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 14));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 15));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 17));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 18));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 19));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 20));
    }

    private void addBybitUsdtRub() {
        sourceService.add(new RateSource(
            0,
            "Bybit",
            "https://www.bybit.com/x-api/fiat/otc/item/online",
            RequestType.POST,
            PayloadType.JSON,
            "{\"userId\":\"\",\"tokenId\":\"\",\"currencyId\":\"\",\"payment\":[],\"side\":\"\",\"size\":\"100\",\"page\":\"1\",\"amount\":\"\",\"vaMaker\":false,\"bulkMaker\":false,\"canTrade\":true,\"verificationFilter\":0,\"sortType\":\"\",\"paymentPeriod\":[],\"itemRegion\":1}",
            null,
            "$.tokenId",
            "$.currencyId",
            "$.side",
            "1",
            "0",
            "{\"User-Agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36\",\"sec-fetch-dest\":\"empty\"}",
            1000,
            "$.price",
            "$.result.items",
            "$.result.items",
            SourceStatus.UP));

        filterService.add(new Filter(0,1, "$.vaMaker", "Show only Verified Advertisers", JsonValueType.BOOLEAN));
        filterService.add(new Filter(0,1, "$.bulkMaker", "Show only Block Advertisers", JsonValueType.BOOLEAN));
        filterService.add(new Filter(0,1, "$.canTrade", "Show only Eligible Ads", JsonValueType.BOOLEAN));
        filterService.add(new Filter(0,1, "$.verificationFilter", "Ads With No Verification Required", JsonValueType.NUMBER));
        filterService.add(new Filter(0,1, "$.sortType", "Sort By", JsonValueType.STRING));
        filterService.add(new Filter(0,1, "$.paymentPeriod", "Payment Time Limit (minutes)", JsonValueType.NUMBER));
        filterService.add(new Filter(0,1, "$.payment", "Payment Methods", JsonValueType.STRING));

        filterValueService.add(new FilterValue(0, 1, null, "Show only Verified Advertisers", "true"));
        filterValueService.add(new FilterValue(0, 1, null, "Show not only Verified Advertisers", "false"));
        filterValueService.add(new FilterValue(0, 2, null, "Show only Block Advertisers", "true"));
        filterValueService.add(new FilterValue(0, 2, null, "Show not only Block Advertisers", "false"));
        filterValueService.add(new FilterValue(0, 3, null, "Show only Eligible Ads", "true"));
        filterValueService.add(new FilterValue(0, 3, null, "Show not only Eligible Ads", "false"));
        filterValueService.add(new FilterValue(0, 4, null, "Only Ads With No Verification Required", "2"));
        filterValueService.add(new FilterValue(0, 4, null, "Not Only Ads With No Verification Required", "0"));
        filterValueService.add(new FilterValue(0, 5, null, "Overall sorting", "OVERALL_RANKING"));
        filterValueService.add(new FilterValue(0, 5, null, "Completed order number", "TRADE_VOLUME"));
        filterValueService.add(new FilterValue(0, 5, null, "Completion Rate", "TRADE_COMPLETION_RATE"));
        filterValueService.add(new FilterValue(0, 5, null, "Price (lowest to highest)", "TRADE_PRICE"));
        filterValueService.add(new FilterValue(0, 6, null, "15 minutes", "15"));
        filterValueService.add(new FilterValue(0, 6, null, "30 minutes", "30"));
        filterValueService.add(new FilterValue(0, 7, 4, "Mobile Top-up", "40"));
        filterValueService.add(new FilterValue(0, 7, 4, "Cash in Person", "90"));
        filterValueService.add(new FilterValue(0, 7, 4, "Bank Transfer", "14"));
        filterValueService.add(new FilterValue(0, 7, 4, "Cash Deposit to Bank", "18"));

        currencyPairPerSourceService.add(new CurrencyPairPerSource(1, 1, "USDT", JsonValueType.STRING, "RUB", JsonValueType.STRING));
        settingService.add(new RateBroadcastingSetting(0, "Bybit USDT-RUB", 1, 1, 1, OrderType.BUY, 1, 9, 1000, BroadcastingStatus.INACTIVE));

        filterValuePerSettingService.add(new FilterValuePerSetting(1, 1));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 4));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 6));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 8));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 12));
        filterValuePerSettingService.add(new FilterValuePerSetting(1, 17));
    }
}
