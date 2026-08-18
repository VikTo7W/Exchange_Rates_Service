package com.yourcompany.rates.api.controller.rates;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.data.aggregate.FixRateAggregate;
import com.yourcompany.rates.data.model.FixRate;
import com.yourcompany.rates.service.aggregation.FixRateAggregateService;
import com.yourcompany.rates.service.crud.FixRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/rates")
public class FixRatesController {

    private final FixRateService service;
    private final FixRateAggregateService aggregateService;
    private final Logger log = Logger.getLogger(FixRatesController.class.getName());

    public FixRatesController(FixRateService service, FixRateAggregateService aggregateService) {
        this.service = service;
        this.aggregateService = aggregateService;
    }

    @GetMapping("/{settingId}/latest")
    public ApiResponse<FixRate> latest(@PathVariable("settingId") int settingId) {
        log.info("Getting FixRate record");
        ApiResponse<FixRate> response = new ApiResponse<>(
            true,
            service.getLatestPerSetting(settingId),
            null
        );
        log.info("FixRate record retrieved successfully");
        return response;
    }

    @GetMapping("/{settingId}")
    public ApiResponse<List<FixRate>> settingHistory(
        @PathVariable("settingId") int settingId
    ) {
        log.info("Getting FixRate records");
        ApiResponse<List<FixRate>> response = new ApiResponse<>(
            true,
            service.getAllPerSetting(settingId),
            null
        );
        log.info("FixRate records retrieved successfully");
        return response;
    }

    @GetMapping("/history")
    public ApiResponse<List<FixRate>> history(
    ) {
        log.info("Getting FixRate records");
        ApiResponse<List<FixRate>> response = new ApiResponse<>(
            true,
            service.getAll(),
            null
        );
        log.info("FixRate records retrieved successfully");
        return response;
    }

    @GetMapping("/aggregate/{settingId}/latest")
    public ApiResponse<FixRateAggregate> getAggregateLatestBySettingId(@PathVariable("settingId") int settingId) {
        log.info("Getting FixRateAggregate record");
        ApiResponse<FixRateAggregate> response = new ApiResponse<>(
            true,
            aggregateService.getAggregateLatestBySettingId(settingId),
            null
        );
        log.info("FixRateAggregate record retrieved successfully");
        return response;
    }

    @GetMapping("/aggregate/{settingId}")
    public ApiResponse<List<FixRateAggregate>> settingHistoryAggregates(
        @PathVariable("settingId") int settingId
    ) {
        log.info("Getting FixRateAggregate records");
        ApiResponse<List<FixRateAggregate>> response = new ApiResponse<>(
            true,
            aggregateService.getAggregatesBySettingId(settingId),
            null
        );
        log.info("FixRateAggregate records retrieved successfully");
        return response;
    }
}

