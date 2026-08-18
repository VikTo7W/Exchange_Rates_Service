package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.model.CurrencyPair;
import com.yourcompany.rates.data.repository.CurrencyPairRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyPairService {

    private final CurrencyPairRepository repository;

    public CurrencyPairService(CurrencyPairRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CurrencyPair add(CurrencyPair pair) {
        return repository.insert(pair);
    }

    @Transactional
    public CurrencyPair getById(int pairId) {
        return repository.findById(pairId).orElseThrow(() -> new DbItemNotFoundException("CurrencyPair instance not found"));
    }

    @Transactional
    public List<CurrencyPair> getAll() {
        return repository.findAll();
    }

    @Transactional
    public CurrencyPair delete(int pairId) {
        return repository.deleteById(pairId).orElseThrow(() -> new DbItemNotFoundException("CurrencyPair instance not found"));
    }
}
