package com.yourcompany.rates.api.controller.admin;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.api.dto.RateSources.*;
import com.yourcompany.rates.data.aggregate.RateSourceAggregate;
import com.yourcompany.rates.data.enums.SourceStatus;
import com.yourcompany.rates.data.model.RateSource;
import com.yourcompany.rates.service.aggregation.RateSourceAggregateService;
import com.yourcompany.rates.service.crud.RateSourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin/sources")
public class RateSourcesController {

    private final RateSourceService service;
    private final RateSourceAggregateService aggregateService;
    private final Logger log = Logger.getLogger(RateSourcesController.class.getName());

    public RateSourcesController(RateSourceService service, RateSourceAggregateService aggregateService) {
        this.service = service;
        this.aggregateService = aggregateService;
    }

    @PostMapping
    public ApiResponse<RateSource> create(
        @RequestBody RateSource payload
    ) {
        log.info("Creating RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.add(payload), "Created");
        log.info("RateSource record created successfully");
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<RateSource> get(@PathVariable("id") int id) {
        log.info("Getting RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.getById(id), null);
        log.info("RateSource record retrieved successfully");
        return response;
    }

    @GetMapping
    public ApiResponse<List<RateSource>> getAll() {
        log.info("Getting RateSource records");
        ApiResponse<List<RateSource>> response = new ApiResponse<>(true, service.getAll(), null);
        log.info("RateSource records retrieved successfully");
        return response;
    }

    @GetMapping(params = "status")
    public ApiResponse<List<RateSource>> getByStatus(@RequestParam("status") SourceStatus status) {
        log.info("Getting RateSource records");
        ApiResponse<List<RateSource>> response = new ApiResponse<>(true, service.getByStatus(status), null);
        log.info("RateSource records retrieved successfully");
        return response;
    }

    @PutMapping("/{id}/changeStatus")
    public ApiResponse<RateSource> changeStatus(
        @PathVariable("id") int id,
        @RequestBody ChangeStatusBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changeStatus(id, payload.newStatus()), "Status updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changePayload")
    public ApiResponse<RateSource> changePayload(
        @PathVariable("id") int id,
        @RequestBody ChangePayloadBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changePayload(id, payload.newPayload()), "Payload updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changePayloadCurrencyFormat")
    public ApiResponse<RateSource> changePayloadCurrencyFormat(
        @PathVariable("id") int id,
        @RequestBody ChangePayloadCurrencyFormatBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changePayloadCurrencyFormat(id, payload.payloadCurrencyFormat()), "PayloadCurrencyFormat updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changePayloadFromCurrencyPath")
    public ApiResponse<RateSource> changePayloadFromCurrencyPath(
        @PathVariable("id") int id,
        @RequestBody ChangePayloadFromCurrencyPathBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changePayloadFromCurrencyPath(id, payload.newPayloadFromCurrencyPath()), "PayloadFromCurrencyPath updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changePayloadToCurrencyPath")
    public ApiResponse<RateSource> changePayloadToCurrencyPath(
        @PathVariable("id") int id,
        @RequestBody ChangePayloadToCurrencyPathBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changePayloadToCurrencyPath(id, payload.newPayloadToCurrencyPath()), "PayloadToCurrencyPath Updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changePayloadOrdersTypeKeyPath")
    public ApiResponse<RateSource> changePayloadOrdersTypeKeyPath(
        @PathVariable("id") int id,
        @RequestBody ChangePayloadOrdersTypeKeyPathBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changePayloadOrdersTypeKeyPath(id, payload.newPayloadOrdersTypeKeyPath()), "PayloadOrdersTypeKeyPath updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changePayloadSellOrdersValue")
    public ApiResponse<RateSource> changePayloadSellOrdersValue(
        @PathVariable("id") int id,
        @RequestBody ChangePayloadSellOrdersValueBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changePayloadSellOrdersValue(id, payload.newPayloadSellOrdersValue()), "PayloadSellOrdersValue updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changePayloadBuyOrdersValue")
    public ApiResponse<RateSource> changePayloadBuyOrdersValue(
        @PathVariable("id") int id,
        @RequestBody ChangePayloadBuyOrdersValueBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changePayloadBuyOrdersValue(id, payload.newPayloadBuyOrdersValue()), "PayloadBuyOrdersValue updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changeHeaders")
    public ApiResponse<RateSource> changeHeaders(
        @PathVariable("id") int id,
        @RequestBody ChangeHeadersBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changeHeaders(id, payload.newHeaders()), "Headers updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changeRateKeyPath")
    public ApiResponse<RateSource> changeRateKeyPath(
        @PathVariable("id") int id,
        @RequestBody ChangeRateKeyPathBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changeRateKeyPath(id, payload.newRateKeyPath()), "RateKeyPath updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changeBuyOrdersArrayKeyPath")
    public ApiResponse<RateSource> changeBuyOrdersArrayKeyPath(
        @PathVariable("id") int id,
        @RequestBody ChangeBuyOrdersArrayKeyPathBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changeBuyOrdersArrayKeyPath(id, payload.newBuyOrdersArrayKeyPath()), "BuyOrdersArrayKeyPath updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @PutMapping("/{id}/changeSellOrdersArrayKeyPath")
    public ApiResponse<RateSource> changeSellOrdersArrayKeyPath(
        @PathVariable("id") int id,
        @RequestBody ChangeSellOrdersArrayKeyPathBody payload
    ) {
        log.info("Changing RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.changeSellOrdersArrayKeyPath(id, payload.newSellOrdersArrayKeyPath()), "SellOrdersArrayKeyPath updated");
        log.info("RateSource record changed successfully");
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<RateSource> delete(@PathVariable("id") int id) {
        log.info("Deleting RateSource record");
        ApiResponse<RateSource> response = new ApiResponse<>(true, service.delete(id), "Deleted");
        log.info("RateSource record deleted successfully");
        return response;
    }

    @GetMapping("/aggregate/{id}")
    public ApiResponse<RateSourceAggregate> getAggregate(
        @PathVariable("id") int id,
        @RequestParam(name = "currencyId", required = false) Integer currencyId
        ) {
        log.info("Getting RateSourceAggregate record");
        ApiResponse<RateSourceAggregate> response = new ApiResponse<>(true, aggregateService.getAggregateById(id, currencyId), null);
        log.info("RateSourceAggregate record retrieved successfully");
        return response;
    }
}

