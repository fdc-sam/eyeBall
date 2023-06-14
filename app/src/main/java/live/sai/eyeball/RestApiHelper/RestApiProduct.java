package live.sai.eyeball.RestApiHelper;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import live.sai.eyeball.Model.Product;
import live.sai.eyeball.NetwortHelper.BuilderHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestApiProduct {

    private static final String TAG = RestApiProduct.class.getSimpleName();

    private Context context;
    private RequestQueue requestQueue;

    public RestApiProduct(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void getAllProducts(ProductListCallback callback) {
        final String API_NAME = "get_all_product";
        // Create endpoint for the API
        final String END_POINT = BuilderHelper.UrlBuilder(API_NAME);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, END_POINT, null,
                response -> {
                    List<Product> productList = new ArrayList<>();
                    try {
                        JSONArray productArray = response.getJSONArray("product");
                        // Parse the JSON response and create Product objects
                        for (int i = 0; i < productArray.length(); i++) {
                            JSONObject productJson = productArray.getJSONObject(i);
                            String id = productJson.getString("id");
                            String name = productJson.getString("name");
                            String type = productJson.getString("type");
                            String cost = productJson.getString("cost");
                            String stocks = productJson.getString("stocks");
                            String lowLevelStock = productJson.getString("low_level_stock");
                            String dateCreate = productJson.getString("date_create");
                            String dateModified = productJson.getString("date_modified");
                            // Retrieve other product details similarly

                            // Create a new Product object and add it to the list
                            Product product = new Product(id, name, type, cost, stocks, lowLevelStock, dateCreate, dateModified);
                            productList.add(product);
                        }

                        // Pass the list of products to the callback
                        callback.onSuccess(productList);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        callback.onError("JSON parsing error");
                    }
                },
                error -> {
                    Log.e(TAG, "API request failed: " + error.getMessage());
                    callback.onError("API request failed");
                });

        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);
    }

    public interface ProductListCallback {
        void onSuccess(List<Product> data);

        void onFailure(String errorMessage);

        void onError(String errorMessage);
    }

    public void saveProduct(Product product, final CommonCallback callback) {
        final String API_NAME = "add_product";
        // Create endpoint for the API
        final String END_POINT = BuilderHelper.UrlBuilder(API_NAME);

        // Create a JSON object with the product data
        JSONObject productJson = new JSONObject();
        try {
            productJson.put("name", product.getName());
            productJson.put("type", product.getType());
            productJson.put("cost", product.getCost());
            productJson.put("stocks", product.getStocks());
            productJson.put("low_level_stock", product.getLowLevelStock());
            productJson.put("image", Base64.encodeToString(product.getImage(), Base64.DEFAULT)); // Add the image data to the JSON object
            // Add other product details similarly
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object: " + e.getMessage());
            callback.onError("Error creating JSON object");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,END_POINT,productJson,
                response -> {

                    try {
                        if (response.has("error")) {
                            // Error occurred
                            JSONObject errorObject = response.getJSONObject("error");
                            callback.onSuccess();
                        } else {
                            callback.onFailure("Failed to save product");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
                        callback.onFailure("Failed to save product");
                    }
                },
                error -> {
                    Log.e(TAG, "API request failed: " + error.getMessage());
                    callback.onFailure("Failed to save product");
                });

        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);
    }


    public interface CommonCallback {
        void onSuccess();

        void onFailure(String errorMessage);

        void onError(String errorMessage);
    }
}
