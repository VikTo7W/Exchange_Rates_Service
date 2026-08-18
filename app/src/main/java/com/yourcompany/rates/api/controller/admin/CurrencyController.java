package com.yourcompany.rates.api.controller.admin;

import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.api.dto.Currency.ChangeRateBody;
import com.yourcompany.rates.data.enums.CurrencyType;
import com.yourcompany.rates.data.model.Currency;
import com.yourcompany.rates.service.crud.CurrencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin/currencies")
public class CurrencyController {

    private final CurrencyService service;
    private final Logger log = Logger.getLogger(CurrencyController.class.getName());

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<Currency> create(@RequestBody Currency currency) {
        log.info("Creating Currency record");
        ApiResponse<Currency> response = new ApiResponse<>(true, service.add(currency), "Created");
        log.info("Currency record created successfully");
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<Currency> get(@PathVariable("id") int id) {
        log.info("Getting Currency record");
        ApiResponse<Currency> response = new ApiResponse<>(true, service.getById(id), null);
        log.info("Currency record retrieved successfully");
        return response;
    }

    @GetMapping(params = "type")
    public ApiResponse<List<Currency>> getByType(@RequestParam("type") CurrencyType type) {
        log.info("Getting Currency records");
        ApiResponse<List<Currency>> response = new ApiResponse<>(true, service.getByType(type), null);
        log.info("Currency records retrieved successfully");
        return response;
    }

    @GetMapping
    public ApiResponse<List<Currency>> getAll() {
        log.info("Getting Currency records");
        ApiResponse<List<Currency>> response = new ApiResponse<>(true, service.getAll(), null);
        log.info("Currency records retrieved successfully");
        return response;
    }

    @PutMapping("/{id}/changeRate")
    public ApiResponse<Currency> changeRate(@PathVariable("id") int id, @RequestBody ChangeRateBody payload) {
        log.info("Changing Currency record");
        ApiResponse<Currency> response = new ApiResponse<>(true, service.changeRate(id, payload.newRate()), "Multiplication rate updated");
        log.info("Currency record changed successfully");
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Currency> delete(@PathVariable("id") int id) {
        log.info("Deleting Currency record");
        ApiResponse<Currency> response = new ApiResponse<>(true, service.delete(id), "Deleted");
        log.info("Currency record deleted successfully");
        return response;
    }
}
