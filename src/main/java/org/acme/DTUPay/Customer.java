package org.acme.DTUPay;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    public String customerID;
    public List<Transfer> transfers;

    public Customer(String id) {
        this.customerID = id;
        this.transfers = new ArrayList<>();
    }
}
