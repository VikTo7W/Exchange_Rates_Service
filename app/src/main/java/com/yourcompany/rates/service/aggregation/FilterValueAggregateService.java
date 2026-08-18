package com.yourcompany.rates.service.aggregation;

import com.yourcompany.rates.data.aggregate.FilterValueAggregate;
import com.yourcompany.rates.data.model.Currency;
import com.yourcompany.rates.data.model.FilterValue;
import com.yourcompany.rates.data.repository.CurrencyRepository;
import com.yourcompany.rates.data.repository.FilterValueRepository;
import com.yourcompany.rates.common.exception.DbItemComponentNotFoundException;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FilterValueAggregateService {

    private final FilterValueRepository filtValueRepo;
    private final CurrencyRepository curRepo;

    public FilterValueAggregateService(FilterValueRepository filtValueRepo, CurrencyRepository curRepo) {
        this.filtValueRepo = filtValueRepo;
        this.curRepo = curRepo;
    }

    @Transactional
    public FilterValueAggregate getAggregateById(int valueId) {

        FilterValue filtValue = filtValueRepo.findById(valueId).orElseThrow(() -> new DbItemNotFoundException("FilterValue instance not found"));
        Currency cur = filtValue.currencyId() != null ? curRepo.findById(filtValue.currencyId()).orElseThrow(() -> new DbItemComponentNotFoundException("Currency instance not found")) : null;

        return new FilterValueAggregate(filtValue, cur);
    }
}
