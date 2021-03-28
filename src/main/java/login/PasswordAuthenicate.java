package login;

import spark.Response;
import spark.Route;

import java.util.ArrayList;

import spark.Request;

import org.json.JSONObject;
import org.postgresql.ds.PGSimpleDataSource;
import java.sql.*;

public class PasswordAuthenicate {

    public static Route passwordAuth = (Request request, Response response) -> {

        String requestBody = request.body();
        System.err.println("Request Body " + requestBody);

        JSONObject jsonObj = new JSONObject(requestBody);
        String emailed = jsonObj.getString("emailSide");
        String passworded = jsonObj.getString("passwordSide");
        System.out.println("Json values: " + emailed + " " + passworded);

        ArrayList fromMatch = loginCheck(emailed, passworded);
        // String sendMatch = Boolean.toString(fromMatch);
        System.out.println("the match: " + fromMatch);

        response.status(200);
        response.type("application/json");

        JSONObject loginReturn = new JSONObject();
        loginReturn.put("login_status", fromMatch.get(0));
        loginReturn.put("name", fromMatch.get(1));
        loginReturn.put("email", fromMatch.get(2));
        loginReturn.put("line1", fromMatch.get(3));
        loginReturn.put("line2", fromMatch.get(4));
        loginReturn.put("city", fromMatch.get(5));
        loginReturn.put("state", fromMatch.get(6));
        loginReturn.put("postal_code", fromMatch.get(7));
        loginReturn.put("phone", fromMatch.get(8));
        loginReturn.put("nameShipping", fromMatch.get(9));
        loginReturn.put("line1Shipping", fromMatch.get(10));
        loginReturn.put("line2Shipping", fromMatch.get(11));
        loginReturn.put("cityShipping", fromMatch.get(12));
        loginReturn.put("stateShipping", fromMatch.get(13));
        loginReturn.put("postal_codeShipping", fromMatch.get(14));

        return loginReturn;
    };

    public static ArrayList loginCheck(String userEmail, String userPassword) {
        String url = "jdbc:postgresql://ec2-3-210-23-22.compute-1.amazonaws.com:5432/da12pint4243b0";
        String databaseName = "da12pint4243b0";
        String userName = "dtfkmtmzdrucem";
        String password = "922a55770da8162f9a80c7a2f9d02a507ac01cbf24e1bf62df4fdd570ced970f";

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

            String st = "SELECT * FROM customer_accounts Where password=? and email=?;";
            // String st = "SELECT * FROM customer_accounts;";

            PreparedStatement ps = conn.prepareStatement(st);
            ps.setString(1, userPassword);
            ps.setString(2, userEmail);

            ResultSet rs = ps.executeQuery();
            // ResultSet rs = ps.executeQuery("SELECT FROM customer_accounts Where email=?
            // and lastName=?;");
            ArrayList<String> custFromDatabase = new ArrayList<String>();
            String correct = "true";
            while (rs.next()) {
                System.out.println("POSTGRES WHILE");

                String email = rs.getString("email");
                String name = rs.getString("firstname");
                String line1 = rs.getString("line1");
                String line2 = rs.getString("line2");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String postal_code = rs.getString("postal_code");
                String phone = rs.getString("phone");

                String nameShipping = rs.getString("nameshipping");
                String line1Shipping = rs.getString("line1shipping");
                String line2Shipping = rs.getString("line2shipping");
                String cityShipping = rs.getString("cityshipping");
                String stateShipping = rs.getString("stateshipping");
                String postal_codeShipping = rs.getString("postal_codeshipping");

                System.out.println("Is it correctn: " + correct);

                custFromDatabase.add(correct);
                custFromDatabase.add(name);
                custFromDatabase.add(email);
                custFromDatabase.add(line1);
                custFromDatabase.add(line2);
                custFromDatabase.add(city);
                custFromDatabase.add(state);
                custFromDatabase.add(postal_code);
                custFromDatabase.add(phone);

                custFromDatabase.add(nameShipping);
                custFromDatabase.add(line1Shipping);
                custFromDatabase.add(line2Shipping);
                custFromDatabase.add(cityShipping);
                custFromDatabase.add(stateShipping);
                custFromDatabase.add(postal_codeShipping);

                System.out.println(" daate received> " + rs.getString(2));
                return custFromDatabase;
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {

        }
        ArrayList<String> holder = new ArrayList<String>();
        holder.add("false");
        return holder;
    }

}