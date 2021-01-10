package org.acme;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.acme.DTUPay.CreateUser;
import org.acme.DTUPay.Transfer;

@Path("/dtupay")
public class DTUPayResource {

    // Map<String, Merchant> merchants = null;
    // Map<String, Customer> customers = null;

    BankService service = (new BankServiceService()).getBankServicePort();

    // public void initializeValues() {
    // merchants = new HashMap<>();
    // customers = new HashMap<>();
    // merchants.put("mid1", new Merchant("mid1"));
    // customers.put("cid1", new Customer("cid1"));
    // }

    @POST
    @Path("/registerUser")
    public Response registerUser(CreateUser data) {
        try {
            var id = service.createAccountWithBalance(data.user, data.balance);
            return Response.ok(id).build();
        } catch (BankServiceException_Exception e) {
            System.err.println("Got error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }

    @GET
    @Path("/getBalance/{id}")
    public Response getBalance(@PathParam String id) {
        try {
            var user = service.getAccount(id);
            if (user == null) {
                return Response.status(Response.Status.PRECONDITION_FAILED).entity("User does not exist").build();
            }
            return Response.ok(user.getBalance()).build();
        } catch (BankServiceException_Exception e) {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }

    @POST
    @Path("/transfer")
    public Response transferMoney(Transfer data) {
        try {
            service.transferMoneyFromTo(data.from, data.to, data.amount, data.description);
            return Response.ok().build();
        } catch (BankServiceException_Exception e) {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }

    @POST
    @Path("/deleteAccount")
    public Response deleteAccount(String id) {
        try {
            service.retireAccount(id);
            return Response.ok().build();
        } catch (BankServiceException_Exception e) {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getCause()).build();
        }
    }

}
