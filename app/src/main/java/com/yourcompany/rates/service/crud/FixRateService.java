package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.model.FixRate;
import com.yourcompany.rates.data.repository.FixRateRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FixRateService {

    private final FixRateRepository repository;

    public FixRateService(FixRateRepository fixRateRepository) {
        this.repository = fixRateRepository;
    }

    @Transactional
    public FixRate add(FixRate rate) {
        return repository.insert(rate);
    }

    @Transactional
    public FixRate getLatestPerSetting(int settingId) {
        return repository.findLatestBySettingId(settingId).orElseThrow(() -> new DbItemNotFoundException("FixRate instance not found"));
    }

    @Transactional
    public List<FixRate> getAllPerSetting(int settingId) {
        return repository.findAllBySettingId(settingId);
    }

    @Transactional
    public List<FixRate> getAll() {
        return repository.findAll();
    }
}
