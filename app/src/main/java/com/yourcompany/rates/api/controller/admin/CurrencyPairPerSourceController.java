package com.yourcompany.rates.api.controller.admin;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.data.model.CurrencyPairPerSource;
import com.yourcompany.rates.service.crud.CurrencyPairPerSourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin/currencyPairsPerSource")
public class CurrencyPairPerSourceController {

    private final CurrencyPairPerSourceService service;
    private final Logger log = Logger.getLogger(CurrencyPairPerSourceController.class.getName());

    public CurrencyPairPerSourceController(CurrencyPairPerSourceService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<CurrencyPairPerSource> create(@RequestBody CurrencyPairPerSource pair) {
        log.info("Creating CurrencyPairPerSource record");
        ApiResponse<CurrencyPairPerSource> response = new ApiResponse<>(true, service.add(pair), "Created");
        log.info("CurrencyPairPerSource record created successfully");
        return response;
    }

    @GetMapping(params = {"sourceId", "pairId"})
    public ApiResponse<CurrencyPairPerSource> get(@RequestParam("sourceId") int sourceId, @RequestParam("pairId") int pairId) {
        log.info("Getting CurrencyPairPerSource record");
        ApiResponse<CurrencyPairPerSource> response = new ApiResponse<>(true, service.getByIds(sourceId, pairId), null);
        log.info("CurrencyPairPerSource record retrieved successfully");
        return response;
    }

    @GetMapping(params = "sourceId")
    public ApiResponse<List<CurrencyPairPerSource>> getBySourceId(@RequestParam("sourceId") int sourceId) {
        log.info("Getting CurrencyPairPerSource records");
        ApiResponse<List<CurrencyPairPerSource>> response = new ApiResponse<>(true, service.getBySourceId(sourceId), null);
        log.info("CurrencyPairPerSource records retrieved successfully");
        return response;
    }

    @DeleteMapping(params = {"sourceId", "pairId"})
    public ApiResponse<CurrencyPairPerSource> delete(@RequestParam("sourceId") int sourceId, @RequestParam("pairId") int pairId) {
        log.info("Deleting CurrencyPairPerSource record");
        ApiResponse<CurrencyPairPerSource> response = new ApiResponse<>(true, service.delete(pairId, sourceId), "Deleted");
        log.info("CurrencyPairPerSource record deleted successfully");
        return response;
    }
}
