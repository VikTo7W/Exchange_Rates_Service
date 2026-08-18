package com.yourcompany.rates.data;

import com.yourcompany.rates.PostgresTestContainer;
import com.yourcompany.rates.data.enums.CurrencyType;
import com.yourcompany.rates.data.model.Currency;
import com.yourcompany.rates.data.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class CurrencyRepositoryTest extends PostgresTestContainer {

    @Autowired
    CurrencyRepository repository;

    @Test
    public void insert_test() {
        Currency inserted = repository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 100));

        assertThat(inserted).isNotNull();

        assertThatThrownBy(() -> repository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 150))).isInstanceOf(Throwable.class);
    }

    @Test
    public void findAll_test() {
        repository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 100));
        repository.insert(new Currency(0, "Euro", "EUR", CurrencyType.FIAT, "€", 100));

        List<Currency> selected = repository.findAll();

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
    }

    @Test
    public void findById_test() {
        repository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 100));

        Optional<Currency> selected = repository.findById(1);

        assertThat(selected).isPresent();
    }

    @Test
    public void findByType_test() {
        repository.insert(new Currency(0, "Crypto dollar", "USDT", CurrencyType.DIGITAL, "USD₮", 100));
        repository.insert(new Currency(0, "Euro", "EUR", CurrencyType.FIAT, "€", 100));

        List<Currency> selected = repository.findByType(CurrencyType.FIAT);

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(1);
        assertThat(selected.get(0).type()).isEqualTo(CurrencyType.FIAT);
    }

    @Test
    public void updateRate_test() {
        repository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 100));

        Optional<Currency> updated = repository.updateRate(1, 1000);

        assertThat(updated).isPresent();
        assertThat(updated.get().rate()).isEqualTo(1000);
    }

    @Test
    public void deleteById_test() {
        repository.insert(new Currency(0, "Dollar", "USD", CurrencyType.FIAT, "$", 100));

        Optional<Currency> deleted = repository.deleteById(1);

        assertThat(deleted).isPresent();
    }
}
