package com.yourcompany.rates.api.controller.admin;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.data.aggregate.FilterValueAggregate;
import com.yourcompany.rates.data.model.FilterValue;
import com.yourcompany.rates.service.aggregation.FilterValueAggregateService;
import com.yourcompany.rates.service.crud.FilterValueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin/filterValues")
public class FilterValueController {

    private final FilterValueService service;
    private final FilterValueAggregateService aggregateService;
    private final Logger log = Logger.getLogger(FilterValueController.class.getName());

    public FilterValueController(FilterValueService service, FilterValueAggregateService aggregateService) {
        this.service = service;
        this.aggregateService = aggregateService;
    }

    @PostMapping
    public ApiResponse<FilterValue> create(@RequestBody FilterValue filterValue) {
        log.info("Creating FilterValue record");
        ApiResponse<FilterValue> response = new ApiResponse<>(true, service.add(filterValue), "Created");
        log.info("FilterValue record created successfully");
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<FilterValue> get(@PathVariable("id") int id) {
        log.info("Getting FilterValue record");
        ApiResponse<FilterValue> response = new ApiResponse<>(true, service.getById(id), null);
        log.info("FilterValue record retrieved successfully");
        return response;
    }

    @GetMapping(params = "filterId")
    public ApiResponse<List<FilterValue>> getByFilterId(@RequestParam("filterId") int filterId) {
        log.info("Getting FilterValue records");
        ApiResponse<List<FilterValue>> response = new ApiResponse<>(true, service.getByFilterId(filterId), null);
        log.info("FilterValue records retrieved successfully");
        return response;
    }

    @GetMapping(params = {"filterId", "currencyId"})
    public ApiResponse<List<FilterValue>> getByFilterAndCurrencyId(@RequestParam("filterId") int filterId, @RequestParam("currencyId") int currencyId) {
        log.info("Getting FilterValue records");
        ApiResponse<List<FilterValue>> response = new ApiResponse<>(true, service.getByFilterAndCurrencyId(filterId, currencyId), null);
        log.info("FilterValue records retrieved successfully");
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<FilterValue> delete(@PathVariable("id") int id) {
        log.info("Deleting FilterValue record");
        ApiResponse<FilterValue> response = new ApiResponse<>(true, service.delete(id), "Deleted");
        log.info("FilterValue record deleted successfully");
        return response;
    }

    @GetMapping("/aggregate/{id}")
    public ApiResponse<FilterValueAggregate> aggregate(@PathVariable("id") int id) {
        log.info("Getting FilterValueAggregate record");
        ApiResponse<FilterValueAggregate> response = new ApiResponse<>(true, aggregateService.getAggregateById(id), null);
        log.info("FilterValueAggregate record retrieved successfully");
        return response;
    }
}
