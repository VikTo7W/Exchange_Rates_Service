package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.model.CurrencyPairPerSource;
import com.yourcompany.rates.data.repository.CurrencyPairPerSourceRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyPairPerSourceService {

    private final CurrencyPairPerSourceRepository repository;

    public CurrencyPairPerSourceService(CurrencyPairPerSourceRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CurrencyPairPerSource add(CurrencyPairPerSource pair) {
        return repository.insert(pair);
    }

    @Transactional
    public List<CurrencyPairPerSource> getBySourceId(int sourceId) {
        return repository.findBySourceId(sourceId);
    }

    @Transactional
    public CurrencyPairPerSource getByIds(int sourceId, int pairId) {
        return repository.findByIds(sourceId, pairId).orElseThrow(() -> new DbItemNotFoundException("CurrencyPairPerSource instance not found"));
    }

    @Transactional
    public CurrencyPairPerSource delete(int pairId, int sourceId) {
        return repository.delete(pairId, sourceId).orElseThrow(() -> new DbItemNotFoundException("CurrencyPairPerSource instance not found"));
    }
}
