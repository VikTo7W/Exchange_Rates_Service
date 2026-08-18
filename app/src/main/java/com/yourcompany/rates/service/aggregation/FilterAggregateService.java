package com.yourcompany.rates.service.aggregation;

import com.yourcompany.rates.data.aggregate.FilterAggregate;
import com.yourcompany.rates.data.aggregate.FilterValueAggregate;
import com.yourcompany.rates.data.model.Filter;
import com.yourcompany.rates.data.model.FilterValue;
import com.yourcompany.rates.data.repository.FilterRepository;
import com.yourcompany.rates.data.repository.FilterValueRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterAggregateService {

    private final FilterRepository filtRepo;
    private final FilterValueAggregateService filtValueService;
    private final FilterValueRepository filtValueRepo;

    public FilterAggregateService(FilterRepository filtRepo, FilterValueAggregateService filtValueService, FilterValueRepository filtValueRepo) {
        this.filtRepo = filtRepo;
        this.filtValueService = filtValueService;
        this.filtValueRepo = filtValueRepo;
    }

    @Transactional
    public FilterAggregate getAggregateById(int filterId, Integer currencyId) {

        Filter filter = filtRepo.findById(filterId).orElseThrow(() -> new DbItemNotFoundException("Filter instance not found"));
        List<FilterValue> filtValues = currencyId == null ? filtValueRepo.findByFilterId(filter.id()) : filtValueRepo.findByFilterAndCurrencyId(filter.id(), currencyId);
        List<FilterValueAggregate> filtValueAggregates = new ArrayList<>();
        filtValues.forEach(filtValue -> filtValueAggregates.add(filtValueService.getAggregateById(filtValue.id())));

        return new FilterAggregate(filter, filtValueAggregates);
    }

}
