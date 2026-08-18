package com.yourcompany.rates.service.messaging;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class RateUpdate {

    private String currencyPair;
    private BigDecimal rate;
    private OffsetDateTime collectionTime;
    private int settingId;

    public RateUpdate() {
    }

    public RateUpdate(String currencyPair, BigDecimal rate, OffsetDateTime collectionTime, int settingId) {
        this.currencyPair = currencyPair;
        this.rate = rate;
        this.collectionTime = collectionTime;
        this.settingId = settingId;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public OffsetDateTime getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(OffsetDateTime collectionTime) {
        this.collectionTime = collectionTime;
    }
}
