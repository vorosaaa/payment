package com.vorosati.payment.project.common;

import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.*;


public enum PaymentResponseType implements ResponseType {
    SUCCESS("0000", "Success", OK),
    INVALID_ACCOUNT("0001", "Invalid account", BAD_REQUEST),
    INVALID_TRANSACTION("0002", "Invalid transaction", BAD_REQUEST),
    ACCOUNT_NOT_FOUND("0003", "Account not found: {0}", NOT_FOUND),
    INSUFFICIENT_BALANCE("0004", "Insufficient balance", BAD_REQUEST),
    INTERNAL_SERVER_ERROR("0005", "Internal server error", Response.Status.INTERNAL_SERVER_ERROR),
    KAFKA_ERROR("0006", "Kafka error", Response.Status.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final Response.Status httpCode;

    PaymentResponseType(String code, String message, Response.Status httpCode) {
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Response.Status getHttpCode() {
        return httpCode;
    }
}
