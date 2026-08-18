package com.yourcompany.rates.service.worker.dto;

import okhttp3.Response;


public record CompletedResponse(
    ActiveSettingContext context,
    Response response
) {
}
