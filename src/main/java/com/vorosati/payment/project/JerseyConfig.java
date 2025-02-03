package com.vorosati.payment.project;

import com.vorosati.payment.project.common.exception.BusinessExceptionMapper;
import com.vorosati.payment.project.component.account.AccountEndpoint;
import com.vorosati.payment.project.component.transaction.TransactionEndpoint;
import jakarta.annotation.PostConstruct;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    private final Logger logger = LoggerFactory.getLogger(JerseyConfig.class.getName());

    @PostConstruct
    public void init() {
        logger.info("Jersey configuration started");
        register(TransactionEndpoint.class);
        register(AccountEndpoint.class);
        register(BusinessExceptionMapper.class);
        logger.info("Endpoints registered successfully");
    }
}