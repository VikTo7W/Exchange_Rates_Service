package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.model.Filter;
import com.yourcompany.rates.data.repository.FilterRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilterService {

    private final FilterRepository repository;

    public FilterService(FilterRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Filter add(Filter filter) {
        return repository.insert(filter);
    }

    @Transactional
    public Filter getById(int filterId) {
        return repository.findById(filterId).orElseThrow(() -> new DbItemNotFoundException("Filter instance not found"));
    }

    @Transactional
    public List<Filter> getBySourceId(int sourceId) {
        return repository.findBySourceId(sourceId);
    }

    @Transactional
    public Filter changeKeyPath(int filterId, String newKey) {
        return repository.updateKeyPath(filterId, newKey).orElseThrow(() -> new DbItemNotFoundException("Filter instance not found"));
    }

    @Transactional
    public Filter delete(int filterId) {
        return repository.deleteById(filterId).orElseThrow(() -> new DbItemNotFoundException("Filter instance not found"));
    }
}
