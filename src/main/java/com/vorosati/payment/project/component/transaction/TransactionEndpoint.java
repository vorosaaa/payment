package com.vorosati.payment.project.component.transaction;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionEndpoint {

    private final TransactionService transactionService;

    public TransactionEndpoint(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @POST
    public Response sendMoney(@QueryParam("sender") Long senderId,
                              @QueryParam("recipient") Long recipientId,
                              @QueryParam("amount") Double amount) {
        Transaction transaction = transactionService.sendMoney(senderId, recipientId, amount);
        return Response.ok(new TransactionResponse(transaction)).build();
    }

    @GET
    @Path("/{accountId}")
    public Response getTransactions(@PathParam("accountId") Long accountId) {
        List<Transaction> transactions = transactionService.getTransactions(accountId);
        List<TransactionResponse> transactionResponses = transactions.stream()
                .map(TransactionResponse::new)
                .toList();
        return Response.ok(transactionResponses).build();
    }
}
