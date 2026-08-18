package com.yourcompany.rates.data;

import com.yourcompany.rates.PostgresTestContainer;
import com.yourcompany.rates.data.enums.RoundingType;
import com.yourcompany.rates.data.enums.StatisticalOperation;
import com.yourcompany.rates.data.model.SettingCalculationRule;
import com.yourcompany.rates.data.repository.SettingCalculationRuleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class SettingCalculationRuleRepositoryTest extends PostgresTestContainer {

    @Autowired
    private SettingCalculationRuleRepository repository;

    @Test
    public void insert_test() {
        assertThatCode(() -> repository.insert(new SettingCalculationRule(0, "Main", new BigDecimal("0.0112"), RoundingType.NEAREST, StatisticalOperation.MEAN))).doesNotThrowAnyException();
    }

    @Test
    public void finById_test() {
        repository.insert(new SettingCalculationRule(0, "Privileged", new BigDecimal("0.0112"), RoundingType.NEAREST, StatisticalOperation.MEAN));
        repository.insert(new SettingCalculationRule(0, "Main", new BigDecimal("0.0155"), RoundingType.NEAREST, StatisticalOperation.MEAN));

        Optional<SettingCalculationRule> selected = repository.findById(1);

        assertThat(selected).isPresent();
        assertThat(selected.get().id()).isEqualTo(1);
    }

    @Test
    public void findAll_test() {
        repository.insert(new SettingCalculationRule(0, "Privileged", new BigDecimal("0.0112"), RoundingType.NEAREST, StatisticalOperation.MEAN));
        repository.insert(new SettingCalculationRule(0, "Main", new BigDecimal("0.0155"), RoundingType.NEAREST, StatisticalOperation.MEAN));

        List<SettingCalculationRule> selected = repository.findAll();

        assertThat(selected).isNotEmpty();
        assertThat(selected.size()).isEqualTo(2);
    }

    @Test
    public void updateMargin_test() {
        repository.insert(new SettingCalculationRule(0, "Privileged", new BigDecimal("0.0112"), RoundingType.NEAREST, StatisticalOperation.MEAN));
        repository.insert(new SettingCalculationRule(0, "Main", new BigDecimal("0.0155"), RoundingType.NEAREST, StatisticalOperation.MEAN));

        Optional<SettingCalculationRule> updated = repository.updateMargin(2, new BigDecimal("0.0211"));

        assertThat(updated).isPresent();
        assertThat(updated.get().id()).isEqualTo(2);
        assertThat(updated.get().marginPercentage()).isEqualTo(new BigDecimal("0.0211"));
    }

    @Test
    public void deleteById_test() {
        repository.insert(new SettingCalculationRule(0, "Privileged", new BigDecimal("0.0112"), RoundingType.NEAREST, StatisticalOperation.MEAN));
        repository.insert(new SettingCalculationRule(0, "Main", new BigDecimal("0.0155"), RoundingType.NEAREST, StatisticalOperation.MEAN));

        Optional<SettingCalculationRule> deleted = repository.deleteById(1);

        assertThat(deleted).isPresent();
        assertThat(deleted.get().id()).isEqualTo(1);
    }
}
