package com.vorosati.payment.project.common.exception;

import com.vorosati.payment.project.common.PaymentResponseType;
import com.vorosati.payment.project.common.ResponseType;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final ResponseType responseType;
    private final Object[] messageParams;

    public BusinessException(ResponseType responseType, Object... messageParams) {
        this.responseType = responseType;
        this.messageParams = messageParams;
    }

    public PaymentResponseType getResponseType() {
        return (PaymentResponseType) responseType;
    }
}
