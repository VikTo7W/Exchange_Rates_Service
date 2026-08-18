package com.yourcompany.rates.api.controller.admin;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.data.model.FilterValuePerSetting;
import com.yourcompany.rates.service.crud.FilterValuePerSettingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin/filterValuesPerSetting")
public class FilterValuePerSettingController {

    private final FilterValuePerSettingService service;
    private final Logger log = Logger.getLogger(FilterValuePerSettingController.class.getName());

    public FilterValuePerSettingController(FilterValuePerSettingService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<FilterValuePerSetting> create(@RequestBody FilterValuePerSetting filterValue) {
        log.info("Creating FilterValuePerSetting record");
        ApiResponse<FilterValuePerSetting> response = new ApiResponse<>(true, service.add(filterValue), "Created");
        log.info("FilterValuePerSetting record created successfully");
        return response;
    }

    @GetMapping(params = "settingId")
    public ApiResponse<List<FilterValuePerSetting>> getBySettingId(@RequestParam("settingId") int settingId) {
        log.info("Getting FilterValuePerSetting records");
        ApiResponse<List<FilterValuePerSetting>> response = new ApiResponse<>(true, service.getBySettingId(settingId), null);
        log.info("FilterValuePerSetting records retrieved successfully");
        return response;
    }

    @DeleteMapping(params = {"settingId", "filterValueId"})
    public ApiResponse<FilterValuePerSetting> delete(@RequestParam("settingId") int settingId, @RequestParam("filterValueId") int filterValueId) {
        log.info("Deleting FilterValuePerSetting record");
        ApiResponse<FilterValuePerSetting> response = new ApiResponse<>(true, service.delete(settingId, filterValueId), "Deleted");
        log.info("FilterValuePerSetting record deleted successfully");
        return response;
    }
}
