package com.yourcompany.rates.service.aggregation;

import com.yourcompany.rates.data.aggregate.FixRateAggregate;
import com.yourcompany.rates.data.aggregate.RateBroadcastingSettingAggregate;
import com.yourcompany.rates.data.model.FixRate;
import com.yourcompany.rates.data.repository.FixRateRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FixRateAggregateService {
    private final FixRateRepository fixRateRepo;
    private final RateBroadcastingSettingAggregateService settingService;

    public FixRateAggregateService(FixRateRepository fixRateRepo, RateBroadcastingSettingAggregateService settingService) {
        this.fixRateRepo = fixRateRepo;
        this.settingService = settingService;
    }

    @Transactional
    public FixRateAggregate getAggregateLatestBySettingId(int settingId) {

        FixRate rate = fixRateRepo.findLatestBySettingId(settingId).orElseThrow(() -> new DbItemNotFoundException("FixRate instance not found"));
        RateBroadcastingSettingAggregate settingAggregate = settingService.getAggregate(rate.settingId());

        return new FixRateAggregate(rate, settingAggregate);
    }

    @Transactional
    public List<FixRateAggregate> getAggregatesBySettingId(Integer settingId) {
        List<FixRate> fixRates = settingId == null ? fixRateRepo.findAll() : fixRateRepo.findAllBySettingId(settingId);
        List<FixRateAggregate> aggregates = new ArrayList<>();
        for (FixRate fixRate : fixRates) {
            RateBroadcastingSettingAggregate setting = settingService.getAggregate(fixRate.settingId());
            aggregates.add(new FixRateAggregate(fixRate, setting));
        }
        return aggregates;
    }
}
