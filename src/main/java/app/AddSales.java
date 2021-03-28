package app;

import java.sql.*;
import java.util.ArrayList;

import org.postgresql.ds.PGSimpleDataSource;

public class AddSales {
    // public ArrayList<String> guestFromPayment;
    public String guestFromPayment;

    // this class adds sales info to Heroku database
    public AddSales(String guestFromPayment) {
        this.guestFromPayment = guestFromPayment;
    }

    public void createAccount() {
        System.out.println("String in AddSales" + guestFromPayment);
        /*
         * /String custID = guestFromPayment.get(0); String email =
         * guestFromPayment.get(1); String name = guestFromPayment.get(2); String line1
         * = guestFromPayment.get(3); String line2 = guestFromPayment.get(4); String
         * city = guestFromPayment.get(5); String state = guestFromPayment.get(6);
         * String postal_code = guestFromPayment.get(7);
         */

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

            /*
             * pst.setString(1, "Guest"); pst.setString(2, email); pst.setString(3, name);
             * pst.setString(4, line1); pst.setString(5, line2); pst.setString(6, city);
             * pst.setString(7, state); pst.setString(8, postal_code);
             */

            int numRowsChanged = pst.executeUpdate();
            System.out.println("Short Result is: " + numRowsChanged);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}