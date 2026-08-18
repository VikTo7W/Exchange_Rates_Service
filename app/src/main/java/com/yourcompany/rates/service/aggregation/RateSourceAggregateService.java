package com.yourcompany.rates.service.aggregation;

import com.yourcompany.rates.data.aggregate.CurrencyPairAggregate;
import com.yourcompany.rates.data.aggregate.FilterAggregate;
import com.yourcompany.rates.data.aggregate.RateSourceAggregate;
import com.yourcompany.rates.data.model.CurrencyPairPerSource;
import com.yourcompany.rates.data.model.Filter;
import com.yourcompany.rates.data.model.RateSource;
import com.yourcompany.rates.data.repository.CurrencyPairPerSourceRepository;
import com.yourcompany.rates.data.repository.FilterRepository;
import com.yourcompany.rates.data.repository.RateSourceRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RateSourceAggregateService {

    private final RateSourceRepository sourceRepo;
    private final CurrencyPairPerSourceRepository currencyPairPerSourceRepo;
    private final CurrencyPairAggregateService curPairService;
    private final FilterRepository filterRepo;
    private final FilterAggregateService filterService;

    public RateSourceAggregateService(RateSourceRepository sourceRepo, CurrencyPairPerSourceRepository currencyPairPerSourceRepo, CurrencyPairAggregateService curPairService, FilterRepository filterRepo, FilterAggregateService filterService) {
        this.sourceRepo = sourceRepo;
        this.currencyPairPerSourceRepo = currencyPairPerSourceRepo;
        this.curPairService = curPairService;
        this.filterRepo = filterRepo;
        this.filterService = filterService;
    }

    @Transactional
    public RateSourceAggregate getAggregateById(int sourceId, Integer currencyId) {

        RateSource source = sourceRepo.findById(sourceId).orElseThrow(() -> new DbItemNotFoundException("RateSource instance not found"));
        List<CurrencyPairPerSource> curPairs = currencyPairPerSourceRepo.findBySourceId(source.id());
        List<CurrencyPairAggregate> curPairsAggregates = new ArrayList<>();
        curPairs.forEach(curPair -> {curPairsAggregates.add(curPairService.getAggregateById(curPair.pairId()));});
        List<Filter> filters = filterRepo.findBySourceId(source.id());
        List<FilterAggregate> filterAggregates = new ArrayList<>();
        filters.forEach(filter -> {filterAggregates.add(filterService.getAggregateById(source.id(), currencyId));});

        return new RateSourceAggregate(source, curPairsAggregates, filterAggregates);
    }
}
