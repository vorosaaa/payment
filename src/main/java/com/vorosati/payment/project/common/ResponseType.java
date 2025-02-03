package com.vorosati.payment.project.common;

import jakarta.ws.rs.core.Response;

public interface ResponseType {
    String getCode();
    String getMessage();

    Response.Status getHttpCode();
}
