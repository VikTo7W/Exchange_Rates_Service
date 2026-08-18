package com.yourcompany.rates.data;

import com.yourcompany.rates.PostgresTestContainer;
import com.yourcompany.rates.data.enums.*;
import com.yourcompany.rates.data.model.Currency;
import com.yourcompany.rates.data.model.Filter;
import com.yourcompany.rates.data.model.FilterValue;
import com.yourcompany.rates.data.model.RateSource;
import com.yourcompany.rates.data.repository.CurrencyRepository;
import com.yourcompany.rates.data.repository.FilterRepository;
import com.yourcompany.rates.data.repository.FilterValueRepository;
import com.yourcompany.rates.data.repository.RateSourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class FilterValueRepositoryTest extends PostgresTestContainer {

    @Autowired
    private FilterValueRepository repository;

    @Autowired
    private FilterRepository filtRepository;

    @Autowired
    private RateSourceRepository sourceRepository;

    @Autowired
    CurrencyRepository curRepository;

    @Test
    public void insert_test() {
        prepareDbEnvironment();

        assertThatCode(() -> repository.insert(new FilterValue(0, 1, null, "Cash in person", "Cash in person"))).doesNotThrowAnyException();
    }

    @Test
    public void findById_test() {
        prepareDbEnvironment();
        repository.insert(new FilterValue(0, 1, null, "Cash in person", "Cash in person"));

        Optional<FilterValue> selected = repository.findById(1);

        assertThat(selected).isPresent();
        assertThat(selected.get().id()).isEqualTo(1);
    }

    @Test
    public void findByFilterAndCurrencyId_test() {
        prepareDbEnvironment();
        curRepository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 100));
        curRepository.insert(new Currency(0, "Euro", "EUR", CurrencyType.FIAT, "€", 100));
        repository.insert(new FilterValue(0, 1, null, "Cash in person", "Cash in person"));
        repository.insert(new FilterValue(0, 1, 2, "Green bank", "Green bank"));
        repository.insert(new FilterValue(0, 1, 1, "Paypal", "Paypal"));
        repository.insert(new FilterValue(0, 2, null, "Verified", "Verified"));

        List<FilterValue> selected = repository.findByFilterAndCurrencyId(1, 2);

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
        assertThat(selected.get(0).value()).isEqualTo("Cash in person");
        assertThat(selected.get(1).value()).isEqualTo("Green bank");
    }

    @Test
    public void findByFilterId_test() {
        prepareDbEnvironment();
        repository.insert(new FilterValue(0, 1, null, "Cash in person", "Cash in person"));
        repository.insert(new FilterValue(0, 1, null, "Green bank", "Green bank"));
        repository.insert(new FilterValue(0, 2, null, "Verified", "Verified"));

        List<FilterValue> selected = repository.findByFilterId(1);

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
        assertThat(selected.get(0).filterId()).isEqualTo(1);
        assertThat(selected.get(1).filterId()).isEqualTo(1);
    }

    @Test
    public void deleteById_test() {
        prepareDbEnvironment();
        repository.insert(new FilterValue(0, 1, null, "Cash in person", "Cash in person"));

        Optional<FilterValue> deleted = repository.deleteById(1);
        Optional<FilterValue> selected = repository.findById(1);

        assertThat(deleted).isPresent();
        assertThat(deleted.get().id()).isEqualTo(1);
        assertThat(selected).isNotPresent();
    }

    private void prepareDbEnvironment() {
        sourceRepository.insert(new RateSource(0, "Binance", "https://binance.com", RequestType.POST, PayloadType.JSON,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        filtRepository.insert(new Filter(0, 1, "payment", "Payment Method", JsonValueType.OBJECT));
        filtRepository.insert(new Filter(0, 1, "verified", "Verification Level", JsonValueType.OBJECT));
    }
}
