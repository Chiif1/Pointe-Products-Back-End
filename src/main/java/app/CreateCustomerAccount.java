package app;

import static com.stripe.model.Customer.create;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.stripe.Stripe;
import com.stripe.model.Customer;

import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.ds.PGSimpleDataSource;

import spark.Request;
import spark.Response;
import spark.Route;

public class CreateCustomerAccount {

        public static Route createCustomer = (final Request request, final Response response) -> {

                final String requestBody = request.body();

                Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");

                final JSONObject jsonCreateCustomer = new JSONObject(requestBody);

                final Map<String, Object> params = new HashMap<>();
                params.put("description", "Customer");
                params.put("name", jsonCreateCustomer.getString("name"));
                params.put("email", jsonCreateCustomer.getString("email"));
                params.put("phone", jsonCreateCustomer.getString("phone"));
                // params.put("password", jsonCreateCustomer.getString("passeord"));

                final JSONObject customerAddress = jsonCreateCustomer.getJSONObject("address");

                final Map<String, Object> paramsCustomerAddress = new HashMap<>();
                paramsCustomerAddress.put("line1", customerAddress.getString("line1"));
                paramsCustomerAddress.put("line2", customerAddress.getString("line2"));
                paramsCustomerAddress.put("city", customerAddress.getString("city"));
                paramsCustomerAddress.put("state", customerAddress.getString("state"));
                paramsCustomerAddress.put("postal_code", customerAddress.getString("postal_code"));

                params.put("address", paramsCustomerAddress);

                final JSONObject shippingObject = jsonCreateCustomer.getJSONObject("shipping");
                final JSONObject addressObject = shippingObject.getJSONObject("address");

                final Map<String, Object> paramsShippingInfo = new HashMap<>();
                paramsShippingInfo.put("name", shippingObject.getString("name"));

                final Map<String, Object> paramsShippingAddress = new HashMap<>();
                paramsShippingAddress.put("line1", addressObject.getString("line1"));
                paramsShippingAddress.put("line2", addressObject.getString("line2"));
                paramsShippingAddress.put("city", addressObject.getString("city"));
                paramsShippingAddress.put("state", addressObject.getString("state"));
                paramsShippingAddress.put("postal_code", addressObject.getString("postal_code"));

                paramsShippingInfo.put("address", paramsShippingAddress);
                params.put("shipping", paramsShippingInfo);

                String password = jsonCreateCustomer.getString("password");
                final Customer customer = create(params);
                String customerID = customer.getId();
                System.out.print("Customer ID: " + customerID);

                response.status(200);
                response.type("application/json");

                final JSONObject customerCreated = new JSONObject();
                customerCreated.put("cust added: ", "customer added");
                customerCreated.put("cust added2: ", "custdomer added2");
                System.out.println("To return: " + customerCreated.toString());

                addCustomerDatabase(jsonCreateCustomer, customerID, password);

                return customerCreated;
        };

        public static void addCustomerDatabase(final JSONObject jsonCreateCustomer, String custID,
                        String password_login) throws JSONException {

                String name = jsonCreateCustomer.getString("name");
                String email = jsonCreateCustomer.getString("email");
                String phone = jsonCreateCustomer.getString("phone");

                JSONObject customerAdrress = jsonCreateCustomer.getJSONObject("address");
                final String line1 = customerAdrress.getString("line1");
                final String line2 = customerAdrress.getString("line2");
                final String city = customerAdrress.getString("city");
                final String state = customerAdrress.getString("state");
                final String postal_code = customerAdrress.getString("postal_code");

                final JSONObject shipping = jsonCreateCustomer.getJSONObject("shipping");
                final String nameShipping = shipping.getString("name");

                final JSONObject addressShipping = shipping.getJSONObject("address");
                final String line1Shipping = addressShipping.getString("line1");
                final String line2Shipping = addressShipping.getString("line2");
                final String cityShipping = addressShipping.getString("city");
                final String stateShipping = addressShipping.getString("state");
                final String postal_codeShipping = addressShipping.getString("postal_code");

                System.out.println("Going to DB: " + stateShipping + " " + postal_codeShipping);

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
                        System.err.println("Connected to the PostgreSQL server successfully.");
                        final Statement st = conn.createStatement();

                } catch (SQLException e) {
                        System.err.println("myapp: " + "e.getMessage()");
                }

                String sql = "insert into customer_accounts values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                // ererrerr
                PreparedStatement pst;
                try {
                        pst = conn.prepareStatement(sql);

                        pst.setString(1, custID);
                        pst.setString(2, name);
                        pst.setString(3, line1);
                        pst.setString(4, line2);
                        pst.setString(5, city);
                        pst.setString(6, state);
                        pst.setString(7, postal_code);
                        pst.setString(8, email);
                        pst.setString(9, nameShipping);
                        pst.setString(10, line1Shipping);
                        pst.setString(11, line2Shipping);
                        pst.setString(12, cityShipping);
                        pst.setString(13, stateShipping);
                        pst.setString(14, postal_codeShipping);
                        pst.setString(15, phone);
                        pst.setString(16, password_login);

                        final int numRowsChanged = pst.executeUpdate();
                        System.out.println("Short Result is: " + numRowsChanged);

                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }
}