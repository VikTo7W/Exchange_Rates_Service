package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.model.FilterValuePerSetting;
import com.yourcompany.rates.data.repository.FilterValuePerSettingRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilterValuePerSettingService {

    private final FilterValuePerSettingRepository repository;

    public FilterValuePerSettingService(FilterValuePerSettingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public FilterValuePerSetting add(FilterValuePerSetting filterValue) {
        return repository.insert(filterValue);
    }

    @Transactional
    public List<FilterValuePerSetting> getBySettingId(int settingId) {
        return repository.findBySettingId(settingId);
    }

    @Transactional
    public FilterValuePerSetting delete(int settingId, int filterValueId) {
        return repository.delete(settingId, filterValueId).orElseThrow(() -> new DbItemNotFoundException("FilterValuePerSetting instance not found"));
    }
}
