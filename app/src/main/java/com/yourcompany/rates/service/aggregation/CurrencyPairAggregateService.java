package com.yourcompany.rates.service.aggregation;

import com.yourcompany.rates.data.aggregate.CurrencyPairAggregate;
import com.yourcompany.rates.data.model.Currency;
import com.yourcompany.rates.data.model.CurrencyPair;
import com.yourcompany.rates.data.repository.CurrencyPairRepository;
import com.yourcompany.rates.data.repository.CurrencyRepository;
import com.yourcompany.rates.common.exception.DbItemComponentNotFoundException;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrencyPairAggregateService {

    private final CurrencyPairRepository curPairRepo;
    private final CurrencyRepository curRepo;

    public CurrencyPairAggregateService(CurrencyPairRepository curPairRepo, CurrencyRepository curRepo) {
        this.curPairRepo = curPairRepo;
        this.curRepo = curRepo;
    }

    @Transactional
    public CurrencyPairAggregate getAggregateById(int pairId) {

        CurrencyPair curPair = curPairRepo.findById(pairId).orElseThrow(() -> new DbItemNotFoundException("CurrencyPair instance not found"));
        Currency curFrom = curRepo.findById(curPair.fromCurrencyId()).orElseThrow(() -> new DbItemComponentNotFoundException("Currency instance not found"));
        Currency curTo = curRepo.findById(curPair.toCurrencyId()).orElseThrow(() -> new DbItemComponentNotFoundException("Currency instance not found"));

        return new CurrencyPairAggregate(curPair, curFrom, curTo);
    }
}
