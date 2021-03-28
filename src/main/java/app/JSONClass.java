package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
//import com.stripe.model.File;
import com.stripe.model.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

public class JSONClass {
    ResultSet resultSet;
    Statement statement;
    Connection connection;
    ArrayList<ProductClass> list;
    JSONObject obj;
    JSONObject obj2;

    JSONClass() {

    }

    public ResultSet createJSOn() {
        String dbUrl = System.getenv("CLEARDB_DATABASE_URL");
        String userName = System.getenv("USER_NAME_CLEARDB");
        String passWord = System.getenv("PASSWORD_CLEARDB");
        String host = "jdbc:mysql://" + System.getenv("HOST_CLEARDB");
        String dataBase = System.getenv("DATABASE_CLEARDB");
        String query = "SELECT * FROM products";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(host, userName, passWord);
            System.err.println("Connection Completed");
            statement = connection.createStatement();
            System.err.println("Statemenmt Made");
            resultSet = statement.executeQuery(query);
            System.err.println("ReslutSet Completed: " + resultSet);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        parseResults(resultSet);

        return resultSet;
    }

    public void parseResults(ResultSet resultSet) {

        list = new ArrayList<ProductClass>();

        try {
            while (resultSet.next()) {

                String productId = resultSet.getString(1);
                System.err.println("Get String");
                String productName = resultSet.getString(2);
                String productPrice = resultSet.getString(3);
                System.err.println("Database Info: " + productId + "   " + productName + "   " + productPrice);

                ProductClass productClass = new ProductClass();

                productClass.setProductName(productName);
                productClass.setProductPrice(productPrice);
                productClass.setProductId(productId);
                System.err.println("Product Class set: " + productClass.getProductName());
                list.add(productClass);
                System.err.println("Array: products added");

            }
            statement.close();
            connection.close();
            fromArrayList();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void fromArrayList() {
        String name1 = list.get(0).productName;
        String price1 = list.get(0).productPrice;
        String id1 = list.get(0).productId;

        String name2 = list.get(1).productName;
        String price2 = list.get(1).productPrice;
        String id2 = list.get(1).productId;
        System.err.println("from Array: " + name1 + name2);
        try {

            JSONObject product1 = new JSONObject();
            product1.put("id", id1);
            product1.put("productName", name1);
            product1.put("productPrice", price1);

            JSONObject obj = new JSONObject();
            obj.put("product", product1);

            JSONObject product2 = new JSONObject();
            product2.put("id", id2);
            product2.put("productName", name2);
            product2.put("productPrice", price2);

            JSONObject obj2 = new JSONObject();
            obj2.put("product", product2);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(obj);
            jsonArray.put(obj2);

            System.err.println(jsonArray.toString());
            try {
                writeJSON(jsonArray);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeJSON(JSONArray jsonArray) throws IOException {

String fileLocation = "C:\\Users\\nugrs\\Heroku Projects\\Stripe Server Backend\\stripe-payments-demo\\server\\java\\src\\main\\java\\app\\jsonFile.tdt";

    File file = new File("TheJsonFile.json");
    
    FileWriter fileWriter = new FileWriter(file);
    fileWriter.write(jsonArray.toString());
    System.err.println("JSon written to file: " + jsonArray.toString());
    fileWriter.flush();
    fileWriter.close();

   BufferedReader br = null;
    br = new BufferedReader(new FileReader(file));
    String line;

    try {
    while ((line = br.readLine()) != null){ 
        System.err.println("Text From File" + line);
    }

    } catch (IOException e) {
        System.err.println("error");
    }finally {
        try {
        br.close();
        } catch (IOException e) {
            System.err.println("Close error");
        }
    }
    System.err.println("The file: " + file.getAbsolutePath());
    }
}
        
               
        