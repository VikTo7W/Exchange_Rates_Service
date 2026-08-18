package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.model.FilterValue;
import com.yourcompany.rates.data.repository.FilterValueRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilterValueService {

    private final FilterValueRepository repository;

    public FilterValueService(FilterValueRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public FilterValue add(FilterValue filterValue) {
        return repository.insert(filterValue);
    }

    @Transactional
    public FilterValue getById(int filterValueId) {
        return repository.findById(filterValueId).orElseThrow(() -> new DbItemNotFoundException("FilterValue instance not found"));
    }

    @Transactional
    public List<FilterValue> getByFilterId(int filterId) {
        return repository.findByFilterId(filterId);
    }

    @Transactional
    public List<FilterValue> getByFilterAndCurrencyId(int filterId, int currencyId) {
        return repository.findByFilterAndCurrencyId(filterId, currencyId);
    }

    @Transactional
    public FilterValue delete(int filterValueId) {
        return repository.deleteById(filterValueId).orElseThrow(() -> new DbItemNotFoundException("FilterValue instance not found"));
    }
}
