package org.acme;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.DTUPay.Customer;
import org.acme.DTUPay.Merchant;
import org.acme.DTUPay.Transfer;

@Path("/dtupay")
public class DTUPayResource {

    Map<String, Merchant> merchants = null;
    Map<String, Customer> customers = null;

    public void initializeValues() {
        merchants = new HashMap<>();
        customers = new HashMap<>();
        merchants.put("mid1", new Merchant("mid1"));
        customers.put("cid1", new Customer("cid1"));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(Transfer transferData) {
        if (merchants == null || customers == null)
            initializeValues();
        var reciver = merchants.get(transferData.merchantID);
        if (reciver == null) {
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity(String.format("merchant with id %s is unknown", transferData.merchantID)).build();
            // return new Error(String.format("merchant with id %s is unknown",
            // transferData.merchantID));
        }
        var provider = customers.get(transferData.customerID);
        if (provider == null) {
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity(String.format("customer with id %s is unknown", transferData.customerID)).build();
        }

        reciver.transfers.add(transferData);
        provider.transfers.add(transferData);

        return Response.status(Response.Status.OK).entity("Transfer complete").build();
    }

    @GET
    @Path("/transactions/{id}")
    // @Produces(MediaType.APPLICATION_JSON)
    public Response getTransfers(@PathParam String id) {
        var merchant = merchants.get(id);
        if (merchant != null) {
            return Response.ok(merchant.transfers.toArray()).build();
        }
        var customer = customers.get(id);
        if (customer != null) {
            return Response.ok(customer.transfers.toArray()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Unknown user").build();
    }

}
