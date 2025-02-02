package com.vorosati.payment.project;

import com.vorosati.payment.project.component.account.AccountEndpoint;
import com.vorosati.payment.project.component.transaction.TransactionEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(TransactionEndpoint.class);
        register(AccountEndpoint.class);
    }
}