package app.payment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.stripe.net.StripeResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.ds.PGSimpleDataSource;

public class StorePurchase {
    String piId;
    String customer;
    String amountIn;
    String date;
    Date dateDate;
    Date dateC;
    LocalDate localDate;
    int amount;
    String receipt_email;

    public StorePurchase() {

    }

    public void purchaseToDatabase(StripeResponse stripeResonResponse, ArrayList<String> guestAccount, boolean guest) {

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
            System.out.println("Connected to Store Purchase the PostgreSQL server successfully.");

        } catch (SQLException e) {
            System.out.println("myapp: " + "e.getMessage()");
        }
        StripeResponse sr = stripeResonResponse;
        String stripeResponeBody = sr.body();
        System.out.print("Stripe --- response body: " + stripeResponeBody);

        try {
            JSONObject stripeResponsejSON = new JSONObject(stripeResponeBody);
            piId = stripeResponsejSON.getString("id");
            customer = stripeResponsejSON.getString("customer");
            // String date = stripeResponsejSON.getString("created");
            Long dateLong = stripeResponsejSON.getLong("created");
            localDate = LocalDate.now();
            receipt_email = stripeResponsejSON.getString("receipt_email");

            amountIn = stripeResponsejSON.getString("amount");
            int amountConvert = Integer.parseInt(amountIn) / 100;

            amount = amountConvert;

            System.out.print("Stripe Json body: " + customer + " " + localDate + " " + amount);
        } catch (JSONException e1) {

            e1.printStackTrace();
        }

        String st = "INSERT INTO payments values (?,?,?,?,?)";
        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(st);

            pst.setString(1, piId);
            pst.setString(2, customer);
            pst.setObject(3, localDate);
            pst.setInt(4, amount);
            pst.setString(5, receipt_email);

            final int numRowsChanged = pst.executeUpdate();
            System.out.println("Short Result is: " + numRowsChanged);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (guest) {
            // saveGuestToDatabase(guestAccount);
        } else {
            System.out.println("Guest not True");
        }
    }

    public void saveGuestToDatabase(ArrayList<String> guestInfo) {

        String custID = guestInfo.get(0);
        String email = guestInfo.get(1);
        String name = guestInfo.get(2);
        String line1 = guestInfo.get(3);
        String line2 = guestInfo.get(4);
        String city = guestInfo.get(5);
        String state = guestInfo.get(6);
        String postal_code = guestInfo.get(7);

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

        Connection conn = null;
        try {
            conn = pgSimpleDataSource.getConnection();
            System.err.println("Connected to AddSales PostgreSQL server successfully.");
            Statement st = conn.createStatement();

        } catch (SQLException e) {
            System.err.println("myapp: " + "e.getMessage()");
        }

        String sql = "insert into customer_accounts (custID, email, firstname, line1, line2, city, state, postal_code) values (?,?,?,?,?,?,?,?)";

        PreparedStatement pst;
        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, email);
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