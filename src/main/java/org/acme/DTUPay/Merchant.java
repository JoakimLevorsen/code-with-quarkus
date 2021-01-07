package org.acme.DTUPay;

import java.util.ArrayList;
import java.util.List;

public class Merchant {
    public String id;
    public List<Transfer> transfers;

    public Merchant(String id) {
        this.id = id;
        this.transfers = new ArrayList<>();
    }
}
