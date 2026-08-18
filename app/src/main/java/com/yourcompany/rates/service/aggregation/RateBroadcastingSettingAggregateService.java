package com.yourcompany.rates.service.aggregation;

import com.yourcompany.rates.data.aggregate.CurrencyPairAggregate;
import com.yourcompany.rates.data.aggregate.FilterValueAggregate;
import com.yourcompany.rates.data.aggregate.RateBroadcastingSettingAggregate;
import com.yourcompany.rates.data.enums.BroadcastingStatus;
import com.yourcompany.rates.data.model.FilterValuePerSetting;
import com.yourcompany.rates.data.model.RateBroadcastingSetting;
import com.yourcompany.rates.data.model.RateSource;
import com.yourcompany.rates.data.model.SettingCalculationRule;
import com.yourcompany.rates.data.repository.*;
import com.yourcompany.rates.common.exception.DbItemComponentNotFoundException;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RateBroadcastingSettingAggregateService {

    private final RateBroadcastingSettingRepository settingRepo;
    private final RateSourceRepository sourceRepo;
    private final CurrencyPairAggregateService curPairRepo;
    private final FilterValueAggregateService filterValueRepo;
    private final FilterValuePerSettingRepository filtValuePerSettingRepo;
    private final SettingCalculationRuleRepository calcRuleRepo;
    private final Logger log;

    public RateBroadcastingSettingAggregateService(RateBroadcastingSettingRepository settingRepo, RateSourceRepository sourceRepo, CurrencyPairAggregateService curPairRepo, FilterValueAggregateService filterValueRepo, FilterValuePerSettingRepository mappingRepo, SettingCalculationRuleRepository calcRuleRepo) {
        this.settingRepo = settingRepo;
        this.sourceRepo = sourceRepo;
        this.curPairRepo = curPairRepo;
        this.filterValueRepo = filterValueRepo;
        this.filtValuePerSettingRepo = mappingRepo;
        this.calcRuleRepo = calcRuleRepo;
        this.log = Logger.getLogger(RateBroadcastingSettingAggregateService.class.getName());
    }

    @Transactional
    public RateBroadcastingSettingAggregate getAggregate(int settingId) {

        RateBroadcastingSetting setting = settingRepo.findById(settingId).orElseThrow(() -> new DbItemNotFoundException("RateBroadcastingSetting instance not found"));
        RateSource source = sourceRepo.findById(setting.sourceId()).orElseThrow(() -> new DbItemComponentNotFoundException("RateSource instance not found"));
        SettingCalculationRule calcRule = calcRuleRepo.findById(setting.calculationRuleId()).orElseThrow(() -> new DbItemComponentNotFoundException("SettingCalculationRule instance not found"));
        CurrencyPairAggregate curPair = curPairRepo.getAggregateById(setting.currencyPairId());
        List<FilterValuePerSetting> filtValues = filtValuePerSettingRepo.findBySettingId(setting.id());
        List<FilterValueAggregate> filtValueAggregates = new ArrayList<>();
        filtValues.forEach(filtValue -> {filtValueAggregates.add(filterValueRepo.getAggregateById(filtValue.filterValueId()));});

        return new RateBroadcastingSettingAggregate(setting, source, calcRule, curPair, filtValueAggregates);
    }

    @Transactional
    public List<RateBroadcastingSettingAggregate> getAggregatesByStatus(BroadcastingStatus status) {

        List<RateBroadcastingSetting> settings = status == null ? settingRepo.findAll() : settingRepo.findByStatus(status);
        List<RateBroadcastingSettingAggregate> settingAggregates = new ArrayList<>();
        settings.forEach(setting -> {
            RateBroadcastingSettingAggregate aggregate;
            try {
                aggregate = getAggregate(setting.id());
            } catch (DbItemNotFoundException | DbItemComponentNotFoundException e) {
                aggregate = null;
                log.warning("Failed to retrieve aggregate of the RateBroadcastingSetting as the setting or its component could not be found");
            }
            settingAggregates.add(aggregate);
        });

        return settingAggregates;
    }
}
