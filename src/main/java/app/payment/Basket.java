package app.payment;

import java.util.*;

public class Basket {
    String currency;
    String receipt_email;
    boolean reviewsd;
    String checkBoxStatus;
    ArrayList<Map> items;
    HashMap<String, String> shippingOption;
    ShippingWay shipping;

    public class ShippingWay {
        String name;
        ShippingAddress address;
    }

    public class ShippingAddress {
        String line1;
        String line2;
        String city;
        String state;
        String postal_code;
    }

    public String getcheckBox() {
        return checkBoxStatus;

    }

}
