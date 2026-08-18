package com.yourcompany.rates.api.controller.admin;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.data.aggregate.CurrencyPairAggregate;
import com.yourcompany.rates.data.model.CurrencyPair;
import com.yourcompany.rates.service.aggregation.CurrencyPairAggregateService;
import com.yourcompany.rates.service.crud.CurrencyPairService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin/currencyPairs")
public class CurrencyPairController {

    private final CurrencyPairService service;
    private final CurrencyPairAggregateService aggregateService;
    private final Logger log = Logger.getLogger(CurrencyPairController.class.getName());

    public CurrencyPairController(CurrencyPairService service, CurrencyPairAggregateService aggregateService) {
        this.service = service;
        this.aggregateService = aggregateService;
    }

    @PostMapping
    public ApiResponse<CurrencyPair> create(@RequestBody CurrencyPair pair) {
        log.info("Creating CurrencyPair record");
        ApiResponse<CurrencyPair> response = new ApiResponse<>(true, service.add(pair), "Created");
        log.info("CurrencyPair record created successfully");
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<CurrencyPair> get(@PathVariable("id") int id) {
        log.info("Getting CurrencyPair record");
        ApiResponse<CurrencyPair> response = new ApiResponse<>(true, service.getById(id), null);
        log.info("CurrencyPair record retrieved successfully");
        return response;
    }

    @GetMapping
    public ApiResponse<List<CurrencyPair>> getAll() {
        log.info("Getting CurrencyPair records");
        ApiResponse<List<CurrencyPair>> response = new ApiResponse<>(true, service.getAll(), null);
        log.info("CurrencyPair records retrieved successfully");
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<CurrencyPair> delete(@PathVariable("id") int id) {
        log.info("Deleting CurrencyPair record");
        ApiResponse<CurrencyPair> response = new ApiResponse<>(true, service.delete(id), "Deleted");
        log.info("CurrencyPair record deleted successfully");
        return response;
    }

    @GetMapping("/aggregate/{id}")
    public ApiResponse<CurrencyPairAggregate> getAggregate(@PathVariable("id") int id) {
        log.info("Getting CurrencyPairAggregate record");
        ApiResponse<CurrencyPairAggregate> response = new ApiResponse<>(true, aggregateService.getAggregateById(id), null);
        log.info("CurrencyPairAggregate record retrieved successfully");
        return response;
    }
}
