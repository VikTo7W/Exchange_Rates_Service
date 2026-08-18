package com.yourcompany.rates.service.crud;

import com.yourcompany.rates.data.enums.BroadcastingStatus;
import com.yourcompany.rates.data.model.RateBroadcastingSetting;
import com.yourcompany.rates.data.repository.RateBroadcastingSettingRepository;
import com.yourcompany.rates.common.exception.DbItemNotFoundException;
import com.yourcompany.rates.common.exception.InvalidSettingOrderWindowException;
import com.yourcompany.rates.service.worker.event.RatesEventPublisher;
import com.yourcompany.rates.service.worker.event.SettingChangedOrActivatedEvent;
import com.yourcompany.rates.service.worker.event.SettingDeactivatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RateBroadcastingSettingService {

    private final RateBroadcastingSettingRepository repository;
    private final RatesEventPublisher eventPublisher;

    public RateBroadcastingSettingService(RateBroadcastingSettingRepository repository, RatesEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public RateBroadcastingSetting add(RateBroadcastingSetting setting) {
        if (setting.ordersWindowStart() > setting.ordersWindowEnd()) {
            throw new InvalidSettingOrderWindowException("orders_window_end cannot be less than orders_window_start");
        }
        return repository.insert(setting);
    }

    @Transactional
    public RateBroadcastingSetting getById(int settingId) {
        return repository.findById(settingId).orElseThrow(() -> new DbItemNotFoundException("RateBroadcastingSetting instance not found"));
    }

    @Transactional
    public List<RateBroadcastingSetting> getByStatus(BroadcastingStatus status) {
        return repository.findByStatus(status);
    }

    @Transactional
    public List<RateBroadcastingSetting> getAll() {
        return repository.findAll();
    }

    @Transactional
    public RateBroadcastingSetting changeStatus(int settingId, BroadcastingStatus newStatus) {
        RateBroadcastingSetting oldSetting = repository.findById(settingId).orElseThrow(() -> new DbItemNotFoundException("RateBroadcastingSetting instance not found"));
        RateBroadcastingSetting newSetting = repository.updateStatus(settingId, newStatus).orElseThrow(() -> new DbItemNotFoundException("RateBroadcastingSetting instance not found"));

        if (oldSetting.broadcastingStatus() == BroadcastingStatus.INACTIVE && newStatus == BroadcastingStatus.ACTIVE) {
            eventPublisher.publish(new SettingChangedOrActivatedEvent(settingId));
        } else if (oldSetting.broadcastingStatus() == BroadcastingStatus.ACTIVE && newStatus == BroadcastingStatus.INACTIVE) {
            eventPublisher.publish(new SettingDeactivatedEvent(settingId, "Deactivated by admin"));
        }
        return newSetting;
    }

    @Transactional
    public RateBroadcastingSetting changeTimeout(int settingId, int timeout) {
        RateBroadcastingSetting oldSetting = repository.findById(settingId).orElseThrow(() -> new DbItemNotFoundException("RateBroadcastingSetting instance not found"));
        RateBroadcastingSetting newSetting = repository.updateTimeout(settingId, timeout).orElseThrow(() -> new DbItemNotFoundException("RateBroadcastingSetting instance not found"));
        if (newSetting.broadcastingStatus() == BroadcastingStatus.ACTIVE && oldSetting.broadcastingTimeout() != timeout) {
            eventPublisher.publish(new SettingChangedOrActivatedEvent(settingId));
        }
        return newSetting;
    }

    @Transactional
    public RateBroadcastingSetting changeSource(int settingId, int sourceId) {
        RateBroadcastingSetting oldSetting = repository.findById(settingId).orElseThrow(() -> new DbItemNotFoundException("RateBroadcastingSetting instance not found"));
        RateBroadcastingSetting newSetting = repository.updateSource(settingId, sourceId).orElseThrow(() -> new DbItemNotFoundException("RateBroadcastingSetting instance not found"));
        if (newSetting.broadcastingStatus() == BroadcastingStatus.ACTIVE && oldSetting.sourceId() != sourceId) {
            eventPublisher.publish(new SettingChangedOrActivatedEvent(settingId));
        }
        return newSetting;
    }

    @Transactional
    public RateBroadcastingSetting delete(int settingId) {
        RateBroadcastingSetting deleted = repository.deleteById(settingId).orElseThrow(() -> new DbItemNotFoundException("RateBroadcastingSetting instance not found"));
        eventPublisher.publish(new SettingDeactivatedEvent(settingId, "Deleted by admin"));
        return deleted;
    }
}
