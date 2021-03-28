package app.seed;

import com.stripe.*;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Sku;
import com.stripe.exception.StripeException;
import java.util.*;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

public class Seed {
    public static void seed() throws StripeException {
        try {
            Seed.seedProducts();
            System.err.println("Seed Product");
        } catch (InvalidRequestException e) {
            System.out.println("Products already exist.");
        }
        try {
            Seed.seedSKUs();
            System.err.println("Seed SKU");
        } catch (InvalidRequestException e) {
            System.out.println("SKUs already exist.");
        }
    }

    private static void seedProducts() throws StripeException {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");

        List<ProductCreateParams> productsToAdd = new ArrayList<ProductCreateParams>();

        ProductCreateParams basketParams = ProductCreateParams.builder().setId("basket").setName("Basket").build();

        productsToAdd.add(basketParams);

        ProductCreateParams cameraParams = ProductCreateParams.builder().setId("camera").setName("Camera").build();

        productsToAdd.add(cameraParams);

        for (ProductCreateParams product : productsToAdd) {
            Product.create(product);
        }

        /*
         * Product productRetrieve = Product.retrieve("Gold-1");
         * 
         * Product deletedProduct = productRetrieve.delete();&/
         * 
         * //Add Camera /*Map<String, Object> table = new HashMap<String, Object>();
         * table.put("id", "table"); table.put("type", "good"); table.put("name",
         * "Table"); ArrayList tableAttrs = new ArrayList(); tableAttrs.add("issue");
         * table.put("attributes", tableAttrs);
         * 
         * productsToAdd.add(table);
         */

        // Add Boot
        /*
         * Map<String, Object> boot = new HashMap<String, Object>(); boot.put("id",
         * "boot"); boot.put("type", "good"); boot.put("name", "City Boot"); ArrayList
         * bootAttrs = new ArrayList(); bootAttrs.add("size"); boot.put("attributes",
         * bootAttrs);
         * 
         * productsToAdd.add(boot);
         * 
         * //Add Increment Magazine Map<String, Object> incrementMagazine = new
         * HashMap<String, Object>(); incrementMagazine.put("id", "increment");
         * incrementMagazine.put("type", "good"); incrementMagazine.put("name",
         * "Increment Magazine"); ArrayList incAttrs = new ArrayList();
         * incAttrs.add("issue"); incrementMagazine.put("attributes", incAttrs);
         * 
         * productsToAdd.add(incrementMagazine);
         * 
         * //Add Stripe Pins Map<String, Object> stripePins = new HashMap<String,
         * Object>(); stripePins.put("id", "pins"); stripePins.put("type", "good");
         * stripePins.put("name", "Stripe Pins"); ArrayList pinsAttrs = new ArrayList();
         * pinsAttrs.add("set"); stripePins.put("attributes", pinsAttrs);
         * 
         * productsToAdd.add(stripePins);
         * 
         * //Add Stripe Pins Map<String, Object> stripeShirt = new HashMap<String,
         * Object>(); stripeShirt.put("id", "shirt"); stripeShirt.put("type", "good");
         * stripeShirt.put("name", "Stripe Shirt"); ArrayList shirtAttrs = new
         * ArrayList(); shirtAttrs.add("size"); shirtAttrs.add("gender");
         * stripeShirt.put("attributes", shirtAttrs);
         * 
         * productsToAdd.add(stripeShirt);
         */

        /*
         * for (Map product: productsToAdd) { Product.create(product); }
         */
    }

    private static void seedSKUs() throws StripeException {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");

        List<PriceCreateParams> priceToAdd = new ArrayList<PriceCreateParams>();

        PriceCreateParams basketParams = PriceCreateParams.builder().setProduct("basket").setNickname("Basket 1")
                .setUnitAmount(3500L).setCurrency("usd").build();

        priceToAdd.add(basketParams);

        PriceCreateParams cameraParams = PriceCreateParams.builder().setProduct("camera").setNickname("Camera 1")
                .setUnitAmount(25000L).setCurrency("usd").build();

        priceToAdd.add(cameraParams);

        for (PriceCreateParams price : priceToAdd) {
            Price.create(price);
        }

        /*
         * Map<String, Object> chainSkuParam = new HashMap<String, Object>();
         * chainSkuParam.put("id", "chain-01"); chainSkuParam.put("price", 44235);
         * chainSkuParam.put("currency", "usd"); chainSkuParam.put("product", "Gold-1");
         * 
         * Sku sku = Sku.create(chainSkuParam);
         */

        // Camera
        /*
         * Map<String, Object> loungeTableSkuParams = new HashMap<String, Object>();
         * loungeTableSkuParams.put("id", "LoungeTable-5");
         * loungeTableSkuParams.put("product", "table");
         * loungeTableSkuParams.put("price", 35300);
         * loungeTableSkuParams.put("currency", "usd"); Map<String, Object>
         * ltableAttrParams = new HashMap<String, Object>();
         * ltableAttrParams.put("size", "Medium");
         * loungeTableSkuParams.put("attributes", ltableAttrParams); Map<String, Object>
         * lTableParams = new HashMap<String, Object>(); lTableParams.put("type",
         * "infinite"); loungeTableSkuParams.put("inventory", lTableParams);
         * 
         * skusToAdd.add(loungeTableSkuParams);
         */

        // Boot
        /*
         * Map<String, Object> bootSkuParams = new HashMap<String, Object>();
         * bootSkuParams.put("id", "brown-boot"); bootSkuParams.put("product", "boot");
         * bootSkuParams.put("price", 23500); bootSkuParams.put("currency", "usd");
         * Map<String, Object> bootAttrParams = new HashMap<String, Object>();
         * bootAttrParams.put("size", "standard"); bootSkuParams.put("attributes",
         * bootAttrParams); Map<String, Object> bootParams = new HashMap<String,
         * Object>(); bootParams.put("type", "infinite"); bootSkuParams.put("inventory",
         * bootParams);
         * 
         * skusToAdd.add(bootSkuParams);
         * 
         * //Increment Magazine Map<String, Object> incrementSkuParams = new
         * HashMap<String, Object>(); incrementSkuParams.put("id", "increment-03");
         * incrementSkuParams.put("product", "increment");
         * incrementSkuParams.put("price", 399); incrementSkuParams.put("currency",
         * "usd"); Map<String, Object> incrementAttrParams = new HashMap<String,
         * Object>(); incrementAttrParams.put("issue", "Issue #3 Development")
         * incrementSkuParams.put("attributes", incrementAttrParams); Map<String,
         * Object> incrementParams = new HashMap<String, Object>();
         * incrementParams.put("type", "infinite"); incrementSkuParams.put("inventory",
         * incrementParams);
         * 
         * skusToAdd.add(incrementSkuParams);
         * 
         * //Stripe Pin Map<String, Object> pinsSkuParams = new HashMap<String,
         * Object>(); pinsSkuParams.put("id", "pins-collector");
         * pinsSkuParams.put("product", "pins"); pinsSkuParams.put("price", 799);
         * pinsSkuParams.put("currency", "usd"); Map<String, Object> pinsAttrParams =
         * new HashMap<String, Object>(); pinsAttrParams.put("set", "Collector Set");
         * pinsSkuParams.put("attributes", pinsAttrParams); Map<String, Object>
         * pinsParams = new HashMap<String, Object>(); pinsParams.put("type", "finite");
         * pinsParams.put("quantity", 500); pinsSkuParams.put("inventory", pinsParams);
         * 
         * skusToAdd.add(pinsSkuParams);
         * 
         * //Stripe Shirt Map<String, Object> shirtSkuParams = new HashMap<String,
         * Object>(); shirtSkuParams.put("id", "shirt-small-woman");
         * shirtSkuParams.put("product", "shirt"); shirtSkuParams.put("price", 999);
         * shirtSkuParams.put("currency", "usd"); Map<String, Object> shirtAttrParams =
         * new HashMap<String, Object>(); shirtAttrParams.put("size", "Small Standard");
         * shirtAttrParams.put("gender", "Woman"); shirtSkuParams.put("attributes",
         * shirtAttrParams); Map<String, Object> shirtParams = new HashMap<String,
         * Object>(); shirtParams.put("type", "infinite");
         * shirtSkuParams.put("inventory", shirtParams);
         * 
         * skusToAdd.add(shirtSkuParams);
         */

        /*
         * for (Map sku: skusToAdd) { Sku.create(sku); }
         */
    }
}
