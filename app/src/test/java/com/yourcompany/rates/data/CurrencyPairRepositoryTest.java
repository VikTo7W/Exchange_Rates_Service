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
import org.springframework.jdbc.UncategorizedSQLException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class CurrencyPairRepositoryTest extends PostgresTestContainer {

    @Autowired
    private CurrencyPairRepository repository;

    @Autowired
    private CurrencyRepository curRepository;

    @Autowired
    private RateSourceRepository sourceRepository;

    @Autowired
    private CurrencyPairPerSourceRepository curPairPerSourceRepository;

    @Test
    public void insert_test() {
        prepareDbEnvironment();

        assertThatThrownBy(() -> repository.insert(new CurrencyPair(0, 1, 1))).isInstanceOf(Throwable.class);
        assertThatCode(() -> repository.insert(new CurrencyPair(0, 1,2))).doesNotThrowAnyException();
    }

    @Test
    public void findById_test() {
        prepareDbEnvironment();
        repository.insert(new CurrencyPair(0, 1, 2));

        Optional<CurrencyPair> selectedExists = repository.findById(1);
        Optional<CurrencyPair> selectedDoesntExist = repository.findById(2);

        assertThat(selectedExists).isPresent();
        assertThat(selectedDoesntExist).isNotPresent();
    }

    @Test
    public void findAll_test() {
        prepareDbEnvironment();
        repository.insert(new CurrencyPair(0, 1, 2));
        repository.insert(new CurrencyPair(0, 2, 1));

        List<CurrencyPair> selected = repository.findAll();

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
    }

    @Test
    public void deleteById_test() {
        prepareDbEnvironment();
        repository.insert(new CurrencyPair(0, 1, 2));
        sourceRepository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.JSON,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        curPairPerSourceRepository.insert(new CurrencyPairPerSource(1, 1, "1", JsonValueType.NUMBER, "0", JsonValueType.NUMBER));

        Optional<CurrencyPair> deletedExists = repository.deleteById(1);
        Optional<CurrencyPair> deletedDoesntExist = repository.deleteById(2);
        List<CurrencyPair> selected = repository.findAll();
        List<CurrencyPairPerSource> selectedPairsPerSource = curPairPerSourceRepository.findBySourceId(1);

        assertThat(deletedExists).isPresent();
        assertThat(deletedDoesntExist).isNotPresent();
        assertThat(selected).isEmpty();
        assertThat(selectedPairsPerSource).isEmpty();
    }

    private void prepareDbEnvironment() {
        curRepository.insert(new Currency(0, "Crypto dollar", "USDT", CurrencyType.DIGITAL, "USD₮", 100));
        curRepository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 100));
    }
}
