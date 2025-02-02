package com.vorosati.payment.project.component.account;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountEndpoint {

    private final AccountService accountService;
    public AccountEndpoint(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Path("/")
    public Response createAccount(
            @RequestBody AccountRequest accountRequest) {
        Account account = accountService.createAccount(accountRequest);
        return Response.ok(new AccountResponse(account)).build();
    }

    @GET
    @Path("/{accountId}")
    public Response getAccount(@PathParam("accountId") Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return Response.ok(new AccountResponse(account)).build();
    }
}