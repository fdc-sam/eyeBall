package live.sai.eyeball.RestApiHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import live.sai.eyeball.Activity.ViewUserDataActivity;
import live.sai.eyeball.NetwortHelper.BuilderHelper;
import live.sai.eyeball.SingleTon.UserDataManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login {

    private static final String TAG = Login.class.getSimpleName();

    private static SharedPreferences sharedPreferences;

    public static void login(Context context, String username, String password, final LoadingDialogCallback callback) {

        sharedPreferences = context.getSharedPreferences("name_pref", Context.MODE_PRIVATE);


        final String API_NAME = "login";
        // - create endpoint for api
        final String END_POINT = BuilderHelper.UrlBuilder(API_NAME);

        // Create JSON object with login credentials
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, END_POINT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onDismiss();
                        try {
                            if (response.has("error")) {
                                // - Error occurred
                                JSONObject errorObject = response.getJSONObject("error");
                                String errorMessage = errorObject.getString("message");
                                handleLoginFailure(context, errorMessage);
                            } else {
                                // - Login successful
                                JSONObject userObject = response.getJSONObject("user_data");
                                UserDataManager.getInstance().setUsername(username);
                                UserDataManager.getInstance().setID(userObject.getString("id"));
                                UserDataManager.getInstance().setEmail(userObject.getString("email"));

                                // - Check if the "Remember Me" checkbox is checked
                                boolean rememberMeChecked = sharedPreferences.getBoolean("remember_me", false);
                                sharedPreferences.edit().putString("user_id", userObject.getString("id")).apply();

                                // - put user data in SharedPreferences
                                Gson gson = new Gson();
                                String userJson = gson.toJson(userObject);
                                // Get an instance of SharedPreferences.Editor
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                // Store the JSON string in SharedPreferences
                                editor.putString("user", userJson);
                                // Save the changes
                                editor.apply();

                                if (rememberMeChecked) {
                                    // - Remember the username and password in SharedPreferences
                                    sharedPreferences.edit().putString("username", username).apply();
                                    sharedPreferences.edit().putString("password", password).apply();
                                } else {
                                    // - Clear the saved username from SharedPreferences
                                    sharedPreferences.edit().remove("username").apply();
                                    sharedPreferences.edit().remove("password").apply();
                                }

                                String userID = UserDataManager.getInstance().getId();
                                String username1 = UserDataManager.getInstance().getUsername();

                                navigateToViewUserDataActivity(context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handleLoginFailure(context, "Login failed. Please try again.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onDismiss();

                        String errorMessage = "Login failed. Please try again.";

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 401) {
                            errorMessage = "Invalid credentials";
                        }

                        Log.e(TAG, "Login Error: " + error.getMessage());
                        handleLoginFailure(context, errorMessage);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                callback.onDismiss();
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                callback.onDismiss();
                if (response != null && response.statusCode == 401) {
                    return Response.error(new VolleyError("Invalid credentials"));
                }

                return super.parseNetworkResponse(response);
            }
        };

        Volley.newRequestQueue(context.getApplicationContext()).add(request);
    }

    private static void handleLoginFailure(Context context, String errorMessage) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private static void navigateToViewUserDataActivity(Context context) {
        Intent intent = new Intent(context, ViewUserDataActivity.class);
        context.startActivity(intent);
    }

    public interface LoadingDialogCallback {
        void onDismiss();
    }
}
