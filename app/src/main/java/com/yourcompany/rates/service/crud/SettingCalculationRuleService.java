package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.enums.BroadcastingStatus;
import com.yourcompany.rates.data.model.RateBroadcastingSetting;
import com.yourcompany.rates.data.model.SettingCalculationRule;
import com.yourcompany.rates.data.repository.RateBroadcastingSettingRepository;
import com.yourcompany.rates.data.repository.SettingCalculationRuleRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import com.yourcompany.rates.service.worker.event.RatesEventPublisher;
import com.yourcompany.rates.service.worker.event.SettingChangedOrActivatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SettingCalculationRuleService {

    private final SettingCalculationRuleRepository repository;
    private final RateBroadcastingSettingRepository broadcastingSettingRepository;
    private final RatesEventPublisher eventPublisher;

    public SettingCalculationRuleService(SettingCalculationRuleRepository repository, RateBroadcastingSettingRepository broadcastingSettingRepository, RatesEventPublisher eventPublisher) {
        this.repository = repository;
        this.broadcastingSettingRepository = broadcastingSettingRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public SettingCalculationRule add(SettingCalculationRule rule) {
        return repository.insert(rule);
    }

    @Transactional
    public SettingCalculationRule getById(int ruleId) {
        return repository.findById(ruleId).orElseThrow(() -> new DbItemNotFoundException("SettingCalculationRule instance not found"));
    }

    @Transactional
    public List<SettingCalculationRule> getAll() {
        return repository.findAll();
    }

    @Transactional
    public SettingCalculationRule changeMargin(int ruleId, BigDecimal marginPercentage) {
        SettingCalculationRule updated = repository.updateMargin(ruleId, marginPercentage).orElseThrow(() -> new DbItemNotFoundException("SettingCalculationRule instance not found"));
        notifyWorker(ruleId);
        return updated;
    }

    @Transactional
    public SettingCalculationRule delete(int ruleId) {
        return repository.deleteById(ruleId).orElseThrow(() -> new DbItemNotFoundException("SettingCalculationRule instance not found"));
    }

    private void notifyWorker(int ruleId) {
        List<RateBroadcastingSetting> activeSettings = broadcastingSettingRepository.findByStatus(BroadcastingStatus.ACTIVE);
        activeSettings.stream().filter(setting -> setting.calculationRuleId() == ruleId).forEach(setting -> eventPublisher.publish(new SettingChangedOrActivatedEvent(setting.id())));
    }
}
