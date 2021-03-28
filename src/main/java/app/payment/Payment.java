package app.payment;

import com.stripe.exception.StripeException;
import app.AddSales;
import app.inventory.Inventory;
import app.seed.Seed;
import com.stripe.model.PaymentIntent;
import com.stripe.net.StripeResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.postgresql.ds.PGSimpleDataSource;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stripe.*;
import java.util.*;
import java.sql.*;

public class Payment {

    private PaymentIntent paymentIntent;
    String custId;

    public Payment(Basket basket) throws StripeException {
        System.err.println("Paymebt class started: ");

        /*
         * try { Seed.seed(); } catch (StripeException e) {
         * 
         * e.printStackTrace(); }
         */
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");

        Map<String, Object> paymentintentParams = new HashMap<String, Object>();
        String receipt_email = basket.receipt_email;

        System.err.println("API key: " + System.getenv("STRIPE_SECRET_KEY"));
        System.err.println("basketTest: " + basket);
        paymentintentParams.put("amount", Inventory.calculatePaymentAmount(basket.items));
        paymentintentParams.put("currency", basket.currency);
        boolean guestAdd = basket.reviewsd;

        if (basket.reviewsd) {

            // do nothting
        } else {
            custId = retreiveCustId(receipt_email);
            paymentintentParams.put("customer", custId);
        }

        paymentintentParams.put("receipt_email", receipt_email);
        System.out.println("The test reviewsd :" + basket.reviewsd);

        // paymentintentParams.remove(" publishable_Key");
        System.err.println("Get Currency: " + basket.currency);
        List<String> payment_method_types = new ArrayList<String>();
        payment_method_types = Arrays
                .asList(Optional.ofNullable(System.getenv("PAYMENT_METHODS")).orElse("card").split("\\s*,\\s*"));
        System.err.println("Paymenet type addded");
        paymentintentParams.put("payment_method_types", payment_method_types);

        // Shipping added to code
        String name = basket.shipping.name;
        String line1 = basket.shipping.address.line1;
        String line2 = basket.shipping.address.line2;
        String city = basket.shipping.address.city;
        String state = basket.shipping.address.state;
        String postal_code = basket.shipping.address.postal_code;

        ArrayList<String> guestToAddSales = new ArrayList<String>();
        guestToAddSales.add(custId);
        guestToAddSales.add(receipt_email);
        guestToAddSales.add(name);
        guestToAddSales.add(line1);
        guestToAddSales.add(line2);
        guestToAddSales.add(city);
        guestToAddSales.add(state);
        guestToAddSales.add(postal_code);

        System.err.println("Customer created in payment");

        Map<String, Object> shippingInfo = new HashMap<String, Object>();
        shippingInfo.put("name", name);
        Map<String, Object> address = new HashMap<String, Object>();
        System.err.println("myapp Payment" + "Where it at");
        address.put("line1", line1);
        address.put("line2", line2);
        address.put("city", city);
        address.put("state", state);
        address.put("postal_code", postal_code);

        shippingInfo.put("address", address);
        paymentintentParams.put("shipping", shippingInfo);

        this.paymentIntent = PaymentIntent.create(paymentintentParams);

        System.out.print("Response Stripe: " + this.paymentIntent.getLastResponse());

        StripeResponse stripeResponse = paymentIntent.getLastResponse();

        StorePurchase storePurchase = new StorePurchase();
        storePurchase.purchaseToDatabase(stripeResponse, guestToAddSales, guestAdd);

    }

    public PaymentIntent getPaymentIntent() {
        return this.paymentIntent;
    }

    public static PaymentIntent updateShippingCost(String paymentIntentId, Basket basket) throws StripeException {

        Long amount = Inventory.calculatePaymentAmount(basket.items);

        amount += Inventory.getShippingCost(basket.shippingOption.get("id"));

        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

        Map<String, Object> paymentintentParams = new HashMap<String, Object>();
        paymentintentParams.put("amount", amount);

        return paymentIntent.update(paymentintentParams);

    }

    public static PaymentIntent getPaymentIntent(String id) throws StripeException {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");

        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);

        return paymentIntent;
    }

    public String retreiveCustId(String email) {
        String url = System.getenv("POSTGRESQL_URL");
        String databaseName = System.getenv("POSTGRESQL_DATABASE_NAME");
        String userName = System.getenv("POSTGRESQL_USER_NAME");
        String password = System.getenv("POSTGRESQL_PASSWORD");

        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUrl(url);
        pgSimpleDataSource.setDatabaseName(databaseName);
        pgSimpleDataSource.setUser(userName);
        pgSimpleDataSource.setPassword(password);

        Connection conn = null;
        try {
            conn = pgSimpleDataSource.getConnection();
            System.out.println("Connected to the PostgreSQL server successfully.");

        } catch (SQLException e) {
            System.out.println("myapp: " + "e.getMessage()");
        }

        try {

            String st = "SELECT * FROM customer_accounts Where email = ?;";

            PreparedStatement ps = conn.prepareStatement(st);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("POSTGRES WHILE");

                String custId = rs.getString("custId");

                System.out.println(" daate received> " + rs.getString(2));

                return custId;
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {

        }
        return "no CustId";
    }

    public void saveGuestToDatabase(ArrayList<String> guestFromPayment) {
        ArrayList<String> guest = guestFromPayment;
        String custID = guest.get(0);
        String email = guest.get(1);
        String name = guest.get(2);
        String line1 = guest.get(3);
        String line2 = guest.get(4);
        String city = guest.get(5);
        String state = guest.get(6);
        String postal_code = guest.get(7);

        System.out.println("arrived in create guest");

        String url = System.getenv("POSTGRESQL_URL");
        String databaseName = System.getenv("POSTGRESQL_DATABASE_NAME");
        String userName = System.getenv("POSTGRESQL_USER_NAME");
        String password = System.getenv("POSTGRESQL_PASSWORD");

        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUrl(url);
        pgSimpleDataSource.setDatabaseName(databaseName);
        pgSimpleDataSource.setUser(userName);
        pgSimpleDataSource.setPassword(password);

        System.out.println("arrived in create guest");
        // Connection conn = null;
        Connection conn = null;
        try {
            conn = pgSimpleDataSource.getConnection();
            System.err.println("Connected to AddSales PostgreSQL server successfully.");
            Statement st = conn.createStatement();

        } catch (SQLException e) {
            System.err.println("myapp: " + "e.getMessage()");
        }

        String sql = "insert into customer_accounts (custID, email, name, line1, line2, city, state, postal_code) values (?,?,?,?,?,?,?,?)";

        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, "Guest");
            pst.setString(2, email);
            pst.setString(3, name);
            pst.setString(4, line1);
            pst.setString(5, line2);
            pst.setString(6, city);
            pst.setString(7, state);
            pst.setString(8, postal_code);

            int numRowsChanged = pst.executeUpdate();
            System.out.println("Short Result is: " + numRowsChanged);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
