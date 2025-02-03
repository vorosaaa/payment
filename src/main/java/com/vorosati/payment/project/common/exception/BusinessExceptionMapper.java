package com.vorosati.payment.project.common.exception;

import com.vorosati.payment.project.common.ResponseType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    private static final Logger logger = LoggerFactory.getLogger(BusinessExceptionMapper.class);

    @Override
    public Response toResponse(BusinessException exception) {
        ResponseType responseType = exception.getResponseType();
        logger.error(MessageFormat.format("Business exception occurred: {0} - {1}", responseType.getCode(), responseType.getMessage()));
        return Response.status(responseType.getHttpCode()).entity(exception).build();
    }
}
