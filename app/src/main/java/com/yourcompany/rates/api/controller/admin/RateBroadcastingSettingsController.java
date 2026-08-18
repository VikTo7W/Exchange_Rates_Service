package com.yourcompany.rates.api.controller.admin;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.api.dto.RateBroadcastingSettings.ChangeSourceBody;
import com.yourcompany.rates.api.dto.RateBroadcastingSettings.ChangeStatusBody;
import com.yourcompany.rates.api.dto.RateBroadcastingSettings.ChangeTimeoutBody;
import com.yourcompany.rates.api.dto.RateBroadcastingSettings.CreateSettingBody;
import com.yourcompany.rates.data.aggregate.RateBroadcastingSettingAggregate;
import com.yourcompany.rates.data.enums.BroadcastingStatus;
import com.yourcompany.rates.data.model.FilterValuePerSetting;
import com.yourcompany.rates.data.model.RateBroadcastingSetting;
import com.yourcompany.rates.service.aggregation.RateBroadcastingSettingAggregateService;
import com.yourcompany.rates.service.crud.FilterValuePerSettingService;
import com.yourcompany.rates.service.crud.RateBroadcastingSettingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin/settings")
public class RateBroadcastingSettingsController {

    private final RateBroadcastingSettingService service;
    private final RateBroadcastingSettingAggregateService aggregateService;
    private final FilterValuePerSettingService filterValuePerSettingService;
    private final Logger log = Logger.getLogger(RateBroadcastingSettingsController.class.getName());

    public RateBroadcastingSettingsController(
        RateBroadcastingSettingService service,
        RateBroadcastingSettingAggregateService aggregateService,
        FilterValuePerSettingService filterValuePerSettingService
    ) {
        this.service = service;
        this.aggregateService = aggregateService;
        this.filterValuePerSettingService = filterValuePerSettingService;
    }

    @PostMapping
    public ApiResponse<RateBroadcastingSettingAggregate> create(
        @RequestBody CreateSettingBody payload
    ) {
        log.info("Creating RateBroadcastingSettingAggregate record");
        RateBroadcastingSetting setting = service.add(payload.setting());
        payload.filterValuesIds().forEach(id -> filterValuePerSettingService.add(new FilterValuePerSetting(setting.id(), id)));
        ApiResponse<RateBroadcastingSettingAggregate> response = new ApiResponse<>(
            true,
            aggregateService.getAggregate(setting.id()),
            "Created"
        );
        log.info("RateBroadcastingSettingAggregate record created successfully");
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<RateBroadcastingSetting> get(
        @PathVariable("id") int id
    ) {
        log.info("Getting RateBroadcastingSetting record");
        ApiResponse<RateBroadcastingSetting> response = new ApiResponse<>(true, service.getById(id), null);
        log.info("RateBroadcastingSetting record retrieved successfully");
        return response;
    }

    @GetMapping(params = "status")
    public ApiResponse<List<RateBroadcastingSetting>> getByStatus(
        @RequestParam("status") BroadcastingStatus status
    ) {
        log.info("Getting RateBroadcastingSetting records");
        ApiResponse<List<RateBroadcastingSetting>> response = new ApiResponse<>(true, service.getByStatus(status), null);
        log.info("RateBroadcastingSetting records retrieved successfully");
        return response;
    }

    @GetMapping
    public ApiResponse<List<RateBroadcastingSetting>> getAll() {
        log.info("Getting RateBroadcastingSetting records");
        ApiResponse<List<RateBroadcastingSetting>> response = new ApiResponse<>(true, service.getAll(), null);
        log.info("RateBroadcastingSetting records retrieved successfully");
        return response;
    }

    @PutMapping("/{id}/changeStatus")
    public ApiResponse<RateBroadcastingSetting> changeStatus(
        @PathVariable("id") int id,
        @RequestBody ChangeStatusBody payload
    ) {
        log.info("Changing RateBroadcastingSetting record");
        ApiResponse<RateBroadcastingSetting> response = new ApiResponse<>(true, service.changeStatus(id, payload.newStatus()), "Status updated");
        log.info("RateBroadcastingSetting record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changeTimeout")
    public ApiResponse<RateBroadcastingSetting> changeTimeout(
        @PathVariable("id") int id,
        @RequestBody ChangeTimeoutBody payload
    ) {
        log.info("Changing RateBroadcastingSetting record");
        ApiResponse<RateBroadcastingSetting> response = new ApiResponse<>(true, service.changeTimeout(id, payload.newTimeout()), "Timeout updated");
        log.info("RateBroadcastingSetting record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changeSource")
    public ApiResponse<RateBroadcastingSetting> changeSource(
        @PathVariable("id") int id,
        @RequestBody ChangeSourceBody payload
    ) {
        log.info("Changing RateBroadcastingSetting record");
        ApiResponse<RateBroadcastingSetting> response = new ApiResponse<>(true, service.changeSource(id, payload.newSourceId()), "Source updated");
        log.info("RateBroadcastingSetting record changed successfully");
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<RateBroadcastingSetting> delete(@PathVariable("id") int id) {
        log.info("Deleting RateBroadcastingSetting record");
        ApiResponse<RateBroadcastingSetting> response = new ApiResponse<>(true, service.delete(id), "Deleted");
        log.info("RateBroadcastingSetting record deleted successfully");
        return response;
    }

    @GetMapping("/aggregate/{id}")
    public ApiResponse<RateBroadcastingSettingAggregate> getAggregate(
        @PathVariable("id") int id
    ) {
        log.info("Getting RateBroadcastingSettingAggregate record");
        ApiResponse<RateBroadcastingSettingAggregate> response = new ApiResponse<>(true, aggregateService.getAggregate(id), null);
        log.info("RateBroadcastingSettingAggregate record retrieved successfully");
        return response;
    }

    @GetMapping("/aggregate")
    public ApiResponse<List<RateBroadcastingSettingAggregate>> getAggregatesByStatus(
        @RequestParam("status") BroadcastingStatus status
    ) {
        log.info("Getting RateBroadcastingSettingAggregate records");
        ApiResponse<List<RateBroadcastingSettingAggregate>> response = new ApiResponse<>(true, aggregateService.getAggregatesByStatus(status), null);
        log.info("RateBroadcastingSettingAggregate records retrieved successfully");
        return response;
    }
}

