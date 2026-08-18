package com.yourcompany.rates.api.controller.admin;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.api.dto.SettingCalculationRule.ChangeMarginBody;
import com.yourcompany.rates.data.model.SettingCalculationRule;
import com.yourcompany.rates.service.crud.SettingCalculationRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin/settingCalculationRules")
public class SettingCalculationRuleController {

    private final SettingCalculationRuleService service;
    private final Logger log = Logger.getLogger(SettingCalculationRuleController.class.getName());

    public SettingCalculationRuleController(SettingCalculationRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<SettingCalculationRule> create(@RequestBody SettingCalculationRule rule) {
        log.info("Creating SettingCalculationRule record");
        ApiResponse<SettingCalculationRule> response = new ApiResponse<>(true, service.add(rule), "Created");
        log.info("SettingCalculationRule record created successfully");
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<SettingCalculationRule> get(@PathVariable("id") int id) {
        log.info("Getting SettingCalculationRule record");
        ApiResponse<SettingCalculationRule> response = new ApiResponse<>(true, service.getById(id), null);
        log.info("SettingCalculationRule record retrieved successfully");
        return response;
    }

    @GetMapping
    public ApiResponse<List<SettingCalculationRule>> getAll() {
        log.info("Getting SettingCalculationRule records");
        ApiResponse<List<SettingCalculationRule>> response = new ApiResponse<>(true, service.getAll(), null);
        log.info("SettingCalculationRule records retrieved successfully");
        return response;
    }

    @PutMapping("/{id}/changeMargin")
    public ApiResponse<SettingCalculationRule> changeMargin(@PathVariable("id") int id, @RequestBody ChangeMarginBody payload) {
        log.info("Changing SettingCalculationRule record");
        ApiResponse<SettingCalculationRule> response = new ApiResponse<>(true, service.changeMargin(id, payload.newMargin()), "Margin updated");
        log.info("SettingCalculationRule record changed successfully");
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<SettingCalculationRule> delete(@PathVariable("id") int id) {
        log.info("Deleting SettingCalculationRule record");
        ApiResponse<SettingCalculationRule> response = new ApiResponse<>(true, service.delete(id), "Deleted");
        log.info("SettingCalculationRule record deleted successfully");
        return response;
    }
}
