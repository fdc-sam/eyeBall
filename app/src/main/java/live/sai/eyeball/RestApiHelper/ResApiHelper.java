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

import live.sai.eyeball.Activity.SigninActivity;
import live.sai.eyeball.Activity.ViewUserDataActivity;
import live.sai.eyeball.Inum.Gender;
import live.sai.eyeball.Loader.LoadingDialog;
import live.sai.eyeball.Model.User;
import live.sai.eyeball.NetwortHelper.BuilderHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResApiHelper {
    private static SharedPreferences sharedPreferences;

    private static final String TAG = ResApiHelper.class.getSimpleName();

    // - add user
    public static void addUser(
            Context context,
            String username,
            String password,
            String selectedRole,
            String selectedDay,
            String selectedMonth,
            String selectedYear,
            Gender selectedGender,
            String email,
            String firstName,
            String middleName,
            String lastName,
            final LoadingDialog loadingDialog
    ) {
        final String API_NAME = "add_user";
        // - create endpoint for api
        final String END_POINT = BuilderHelper.UrlBuilder(API_NAME);

        // Create JSON object with user data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("role", selectedRole);
            jsonObject.put("birthDay", selectedDay);
            jsonObject.put("birthMonth", selectedMonth);
            jsonObject.put("birthYear", selectedYear);
            jsonObject.put("gender", selectedGender);
            jsonObject.put("email", email);
            jsonObject.put("firstName", firstName);
            jsonObject.put("middleName", middleName);
            jsonObject.put("lastName", lastName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, END_POINT, jsonObject,
                response -> {
                    loadingDialog.dismissDialog();

                    try {
                        if (response.has("error")) {
                            // Error occurred
                            JSONObject errorObject = response.getJSONObject("error");
                            String errorMessage = errorObject.getString("message");
                            handleAddUserFailure(context, errorMessage);
                        } else {
                            // User added successfully
                            String classActivity = SigninActivity.class.getName();
                            try {
                                Class<?> activityClass = Class.forName(classActivity);
                                navigateToActivity(context, activityClass);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handleAddUserFailure(context, "Failed to add user. Please try again.");
                    }
                },
                error -> {
                    loadingDialog.dismissDialog();

                    String errorMessage = "Failed to add user. Please try again.";

                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.statusCode == 401) {
                        errorMessage = "Invalid credentials";
                    }

                    Log.e(TAG, "Add User Error: " + error.getMessage());
                    handleAddUserFailure(context, errorMessage);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response != null && response.statusCode == 401) {
                    return Response.error(new VolleyError("Invalid credentials"));
                }

                return super.parseNetworkResponse(response);
            }
        };

        Volley.newRequestQueue(context.getApplicationContext()).add(request);
    }

    // - get all user data
    public static void getUserData(ViewUserDataActivity intent, UserDataCallback callback) {
        final String API_NAME = "get_user_data";
        // - create endpoint for api
        final String END_POINT = BuilderHelper.UrlBuilder(API_NAME);

        sharedPreferences = intent.getSharedPreferences("name_pref", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", null);

        // Create JSON object with user data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, END_POINT, jsonObject,
                response -> {
                    List<User> userList = new ArrayList<>();

                    try {
                        JSONArray userDataArray = response.getJSONArray("user_data");
                        for (int i = 0; i < userDataArray.length(); i++) {
                            JSONObject userDataJson = userDataArray.getJSONObject(i);
                            String username = userDataJson.getString("username");
                            String password = userDataJson.getString("password");
                            String role = userDataJson.getString("role");
                            String birthDay = userDataJson.getString("birthDay");
                            String birthMonth = userDataJson.getString("birthMonth");
                            String birthYear = userDataJson.getString("birthYear");
                            Gender gender = Gender.valueOf(userDataJson.getString("gender"));
                            String email = userDataJson.getString("email");
                            String firstName = userDataJson.getString("firstName");
                            String middleName = userDataJson.getString("middleName");
                            String lastName = userDataJson.getString("lastName");
                            String userId = userDataJson.getString("user_id");
                            String userBirthDate = userDataJson.getString("birthDate");
                            User user = new User(
                                    userId,
                                    username,
                                    password,
                                    role,
                                    birthDay,
                                    birthMonth,
                                    birthYear,
                                    gender,
                                    email,
                                    firstName,
                                    middleName,
                                    lastName,
                                    userBirthDate
                            );
                            userList.add(user);
                        }

                        callback.onSuccess(userList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure("Failed to parse user data");
                    }
                },
                error -> {
                    Log.e(TAG, "Get User Data Error: " + error.getMessage());
                    callback.onFailure("Failed to fetch user data");
                });

        Volley.newRequestQueue(intent.getApplicationContext()).add(request);
    }

    // Update user data
    public static void updateUserData(
            Context context,
            User user,
            final LoadingDialog loadingDialog,
            final commonCallback callback
    ) {
        final String API_NAME = "update_user_data";
        // - create endpoint for api
        final String END_POINT = BuilderHelper.UrlBuilder(API_NAME);

        // Create JSON object with updated user data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", user.getUserId());
            jsonObject.put("username", user.getUsername());
            jsonObject.put("role", user.getSelectedRole());
            jsonObject.put("birthDay", user.getSelectedDay());
            jsonObject.put("birthMonth", user.getSelectedMonth());
            jsonObject.put("birthYear", user.getSelectedYear());
            jsonObject.put("gender", user.getGender().toString());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("firstName", user.getFirstName());
            jsonObject.put("middleName", user.getMiddleName());
            jsonObject.put("lastName", user.getLastName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, END_POINT, jsonObject,
                response -> {
                    loadingDialog.dismissDialog();

                    try {
                        if (response.has("error")) {
                            // Error occurred
                            JSONObject errorObject = response.getJSONObject("error");
                            String errorMessage = errorObject.getString("message");
                            callback.onFailure(errorMessage);
                        } else {
                            // User updated successfully
                            JSONObject userObject = response.getJSONObject("user_data");
                            // - put user data in SharedPreferences
                            Gson gson = new Gson();
                            String userJson = gson.toJson(userObject);
                            // Get an instance of SharedPreferences.Editor
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            // Store the JSON string in SharedPreferences
                            editor.putString("user", userJson);
                            // Save the changes
                            editor.apply();

                            callback.onSuccess();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure("Failed to update user. Please try again.");
                    }
                },
                error -> {
                    loadingDialog.dismissDialog();

                    String errorMessage = "Failed to update user. Please try again.";

                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.statusCode == 401) {
                        errorMessage = "Invalid credentials";
                    }

                    Log.e(TAG, "Update User Error: " + error.getMessage());
                    callback.onFailure(errorMessage);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response != null && response.statusCode == 401) {
                    return Response.error(new VolleyError("Invalid credentials"));
                }

                return super.parseNetworkResponse(response);
            }
        };

        Volley.newRequestQueue(context.getApplicationContext()).add(request);
    }

    public static void deleteUserData(
            Context context,
            User user,
            LoadingDialog loadingDialog,
            final commonCallback callback
    ) {
        final String API_NAME = "delete_user_data";
        // - create endpoint for api
        final String END_POINT = BuilderHelper.UrlBuilder(API_NAME);

        // Create JSON object with updated user data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", user.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, END_POINT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingDialog.dismissDialog();

                        try {
                            if (response.has("error")) {
                                // Error occurred
                                JSONObject errorObject = response.getJSONObject("error");
                                String errorMessage = errorObject.getString("message");
                                callback.onFailure(errorMessage);
                            } else {
                                // User deleted successfully
                                callback.onSuccess();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure("Failed to delete user. Please try again.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismissDialog();

                        String errorMessage = "Failed to delete user. Please try again.";

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 401) {
                            errorMessage = "Invalid credentials";
                        }

                        Log.e(TAG, "Delete User Error: " + error.getMessage());
                        callback.onFailure(errorMessage);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response != null && response.statusCode == 401) {
                    return Response.error(new VolleyError("Invalid credentials"));
                }

                return super.parseNetworkResponse(response);
            }
        };

        Volley.newRequestQueue(context.getApplicationContext()).add(request);
    }


    private static void handleAddUserFailure(Context context, String errorMessage) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private static void navigateToActivity(Context context, Class<?> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    public interface UserDataCallback {
        void onSuccess(List<User> data);

        void onFailure(String errorMessage);
    }

    public interface commonCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }
}
