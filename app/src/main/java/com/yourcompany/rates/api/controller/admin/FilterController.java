package com.yourcompany.rates.api.controller.admin;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.api.dto.Filter.ChangeKeyPathBody;
import com.yourcompany.rates.data.aggregate.FilterAggregate;
import com.yourcompany.rates.data.model.Filter;
import com.yourcompany.rates.service.aggregation.FilterAggregateService;
import com.yourcompany.rates.service.crud.FilterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin/filters")
public class FilterController {

    private final FilterService service;
    private final FilterAggregateService aggregateService;
    private final Logger log = Logger.getLogger(FilterController.class.getName());

    public FilterController(FilterService service, FilterAggregateService aggregateService) {
        this.service = service;
        this.aggregateService = aggregateService;
    }

    @PostMapping
    public ApiResponse<Filter> create(@RequestBody Filter filter) {
        log.info("Creating Filter record");
        ApiResponse<Filter> response = new ApiResponse<>(true, service.add(filter), "Created");
        log.info("Filter record created successfully");
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<Filter> get(@PathVariable("id") int id) {
        log.info("Getting Filter record");
        ApiResponse<Filter> response = new ApiResponse<>(true, service.getById(id), null);
        log.info("Filter record retrieved successfully");
        return response;
    }

    @GetMapping
    public ApiResponse<List<Filter>> getBySourceId(@RequestParam("sourceId") int sourceId) {
        log.info("Getting Filter records");
        ApiResponse<List<Filter>> response = new ApiResponse<>(true, service.getBySourceId(sourceId), null);
        log.info("Filter records retrieved successfully");
        return response;
    }

    @PutMapping("/{id}/changeKeyPath")
    public ApiResponse<Filter> changeKeyPath(@PathVariable("id") int id, @RequestBody ChangeKeyPathBody payload) {
        log.info("Changing Filter record");
        ApiResponse<Filter> response = new ApiResponse<>(true, service.changeKeyPath(id, payload.newKeyPath()), "KeyPath updated");
        log.info("Filter record changed successfully");
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Filter> delete(@PathVariable("id") int id) {
        log.info("Deleting Filter record");
        ApiResponse<Filter> response = new ApiResponse<>(true, service.delete(id), "Deleted");
        log.info("Filter record deleted successfully");
        return response;
    }

    @GetMapping("/aggregate/{id}")
    public ApiResponse<FilterAggregate> getAggregate(@PathVariable("id") int id, @RequestParam(name = "currencyId", required = false) Integer currencyId) {
        log.info("Getting FilterAggregate record");
        ApiResponse<FilterAggregate> response = new ApiResponse<>(true, aggregateService.getAggregateById(id, currencyId), null);
        log.info("FilterAggregate record retrieved successfully");
        return response;
    }
}
