package com.yourcompany.rates.data;

import com.yourcompany.rates.PostgresTestContainer;
import com.yourcompany.rates.data.enums.JsonValueType;
import com.yourcompany.rates.data.enums.PayloadType;
import com.yourcompany.rates.data.enums.RequestType;
import com.yourcompany.rates.data.enums.SourceStatus;
import com.yourcompany.rates.data.model.Filter;
import com.yourcompany.rates.data.model.FilterValue;
import com.yourcompany.rates.data.model.RateSource;
import com.yourcompany.rates.data.repository.FilterRepository;
import com.yourcompany.rates.data.repository.FilterValueRepository;
import com.yourcompany.rates.data.repository.RateSourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class FilterRepositoryTest extends PostgresTestContainer {

    @Autowired
    private FilterRepository repository;

    @Autowired
    private RateSourceRepository sourceRepository;

    @Autowired
    private FilterValueRepository filtValuerepository;

    @Test
    public void insert_test() {
        prepareDbEnvironment();

        assertThatCode(() -> repository.insert(new Filter(0, 1, "payment", "Payment Method", JsonValueType.OBJECT))).doesNotThrowAnyException();
    }

    @Test
    public void findBySourceId_test() {
        prepareDbEnvironment();
        repository.insert(new Filter(0, 1, "payment", "Payment Method", JsonValueType.OBJECT));
        repository.insert(new Filter(0, 2, "verified", "Verification Level", JsonValueType.OBJECT));

        List<Filter> selected = repository.findBySourceId(1);

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(1);
        assertThat(selected.get(0).id()).isEqualTo(1);
    }

    @Test
    public void findById_test() {
        prepareDbEnvironment();
        repository.insert(new Filter(0, 1, "payment", "Payment Method", JsonValueType.OBJECT));
        repository.insert(new Filter(0, 2, "verified", "Verification Level", JsonValueType.OBJECT));

        Optional<Filter> selected = repository.findById(1);
        Optional<Filter> notSelected = repository.findById(3);

        assertThat(selected).isPresent();
        assertThat(selected.get().id()).isEqualTo(1);
        assertThat(notSelected).isNotPresent();
    }

    @Test
    public void updateKey_Path_test() {
        prepareDbEnvironment();
        repository.insert(new Filter(0, 1, "payment", "Payment Method", JsonValueType.OBJECT));

        Optional<Filter> updated = repository.updateKeyPath(1, "payment_method");

        assertThat(updated).isPresent();
        assertThat(updated.get().keyPath()).isEqualTo("payment_method");
    }

    @Test
    public void deleteById_test() {
        prepareDbEnvironment();
        repository.insert(new Filter(0, 1, "payment", "Payment Method", JsonValueType.OBJECT));
        filtValuerepository.insert(new FilterValue(0, 1, null, "Cash in person", "Cash in person"));

        Optional<Filter> deleted = repository.deleteById(1);
        List<Filter> selected = repository.findBySourceId(1);
        Optional<FilterValue> selectedFilterValue = filtValuerepository.findById(1);

        assertThat(deleted).isPresent();
        assertThat(selected).isEmpty();
        assertThat(selectedFilterValue).isNotPresent();
    }

    private void prepareDbEnvironment() {
        sourceRepository.insert(new RateSource(0, "Bybit", "https://bybit.com", RequestType.GET, PayloadType.URL_PARAMETERS,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
        sourceRepository.insert(new RateSource(0, "Binance", "https://binance.com", RequestType.POST, PayloadType.JSON,"payload", "@fromCurrency/@toCurrency", "payloadFromCurrencyPath", "payloadToCurrencyPath", "payloadOrdersTypeKeyPath", "payloadBuyOrdersValue", "payloadSellOrdersValue", "{\"Header\": \"header\"}", 10, "keyPath" , "class=\"\"", "keyPath", SourceStatus.UP));
    }
}
