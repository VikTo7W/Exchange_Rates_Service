package com.yourcompany.rates.data;

import com.yourcompany.rates.PostgresTestContainer;
import com.yourcompany.rates.data.enums.*;
import com.yourcompany.rates.data.model.Currency;
import com.yourcompany.rates.data.model.CurrencyPair;
import com.yourcompany.rates.data.model.CurrencyPairPerSource;
import com.yourcompany.rates.data.model.RateSource;
import com.yourcompany.rates.data.repository.CurrencyPairPerSourceRepository;
import com.yourcompany.rates.data.repository.CurrencyPairRepository;
import com.yourcompany.rates.data.repository.CurrencyRepository;
import com.yourcompany.rates.data.repository.RateSourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class CurrencyPairPerSourceRepositoryTest extends PostgresTestContainer {

    @Autowired
    private CurrencyPairPerSourceRepository repository;

    @Autowired
    private CurrencyPairRepository curPairRepository;

    @Autowired
    private CurrencyRepository curRepository;

    @Autowired
    private RateSourceRepository sourceRepository;

    @Test
    public void insert_test() {
        prepareDbEnvironment();

        assertThatCode(() -> repository.insert(new CurrencyPairPerSource(1, 1, "1", JsonValueType.NUMBER, "0", JsonValueType.NUMBER))).doesNotThrowAnyException();
        assertThatThrownBy(() -> repository.insert(new CurrencyPairPerSource(1, 1, "1", JsonValueType.NUMBER, "0", JsonValueType.NULL))).isInstanceOf(Throwable.class);
    }

    @Test
    public void findBySourceId_test() {
        prepareDbEnvironment();
        repository.insert(new CurrencyPairPerSource(1, 1, "1", JsonValueType.NUMBER, "0", JsonValueType.NUMBER));
        repository.insert(new CurrencyPairPerSource(2, 2, "1", JsonValueType.NUMBER, "0", JsonValueType.NUMBER));

        List<CurrencyPairPerSource> selected = repository.findBySourceId(1);

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(1);
        assertThat(selected.get(0).sourceId()).isEqualTo(1);
    }

    @Test
    public void delete_test() {
        prepareDbEnvironment();
        repository.insert(new CurrencyPairPerSource(1, 1, "1", JsonValueType.NUMBER, "0", JsonValueType.NUMBER));

        Optional<CurrencyPairPerSource> deleted = repository.delete(1, 1);
        List<CurrencyPairPerSource> selected = repository.findBySourceId(1);

        assertThat(deleted).isPresent();
        assertThat(selected).isEmpty();
    }

    private void prepareDbEnvironment() {
        curRepository.insert(new Currency(0, "Crypto dollar", "USDT", CurrencyType.DIGITAL, "USD₮", 100));
        curRepository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 100));
        curPairRepository.insert(new CurrencyPair(0, 1, 2));
        curPairRepository.insert(new CurrencyPair(0, 2, 1));
        sourceRepository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        sourceRepository.insert(new RateSource(0, "Binance", "https://binance.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
    }
}
