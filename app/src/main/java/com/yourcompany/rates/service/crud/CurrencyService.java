package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.enums.CurrencyType;
import com.yourcompany.rates.data.model.Currency;
import com.yourcompany.rates.data.repository.CurrencyRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import com.yourcompany.rates.common.exception.InvalidCurrencyMultiplicationRateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurrencyService {

    private final CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Currency add(Currency currency) {
        validateRate(currency.rate());
        return repository.insert(currency);
    }

    @Transactional
    public Currency getById(int currencyId) {
        return repository.findById(currencyId).orElseThrow(() -> new DbItemNotFoundException("Currency instance not found"));
    }

    @Transactional
    public List<Currency> getAll() {
        return repository.findAll();
    }

    @Transactional
    public List<Currency> getByType(CurrencyType type) {
        return repository.findByType(type);
    }

    @Transactional
    public Currency changeRate(int currencyId, int rate) {
        validateRate(rate);
        return repository.updateRate(currencyId, rate).orElseThrow(() -> new DbItemNotFoundException("Currency instance not found"));
    }

    @Transactional
    public Currency delete(int currencyId) {
        return repository.deleteById(currencyId).orElseThrow(() -> new DbItemNotFoundException("Currency instance not found"));
    }

    private void validateRate(int rate) {
        if (rate < 1) {
            throw new InvalidCurrencyMultiplicationRateException("Currency multiplication rate must be a positive power of 10");
        }
        while(rate > 1 && rate % 10 == 0){
            rate /= 10;
        }
        if (rate != 1) throw new InvalidCurrencyMultiplicationRateException("Currency multiplication rate must be a positive power of 10");
    }
}
