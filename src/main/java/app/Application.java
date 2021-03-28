package app;

import app.config.ConfigController;
import app.fulfillment.FulfillmentController;
import app.product.ProductController;
import login.PasswordAuthenicate;
import app.payment.PaymentController;
import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {

        port(getHerokuAssignedPort());

        staticFiles.location("/public");
        System.err.println("Hello, logs!  " + getHerokuAssignedPort());
        staticFiles.expireTime(600L);

        System.err.println("... past Json ......");

        get("/config", ConfigController.getConfig);
        get("/products", ProductController.getProducts);
        get("/products/:id", ProductController.getProduct);
        get("/products/:id/skus", ProductController.getSKUsForProduct);
        get("/payment_intents/:id/status", PaymentController.getPaymentIntent);
        post("/payment_intents", PaymentController.createPaymentIntent);
        post("/payment_intents/:id/shipping_change", PaymentController.updatePaymentIntent);
        post("/webhook", FulfillmentController.webhookReceived);
        post("/password_auth", PasswordAuthenicate.passwordAuth);
        post("/create_customer", CreateCustomerAccount.createCustomer);

    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
    }
}