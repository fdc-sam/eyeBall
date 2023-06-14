package live.sai.eyeball.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import live.sai.eyeball.Fragment.MenuFragment;
import live.sai.eyeball.Inum.Gender;
import live.sai.eyeball.Loader.LoadingDialog;
import live.sai.eyeball.Model.User;
import live.sai.eyeball.R;
import live.sai.eyeball.RestApiHelper.ResApiHelper;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class UpdateUserDataActivity extends AppCompatActivity {

    private Spinner roleSpinner, daySpinner, monthSpinner, yearSpinner, genderSpinner;
    private EditText emailEditText, usernameEditText, passwordEditText, firstNameEditText, middleNameEditText, lastNameEditText, userIdEditText;
    private Button updateButton;
    private String day;
    private User user;
    private static SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_data);
        JsonObject jsonObject = new JsonObject();

        // Set the header for the activity
        setupHeader(true, "handbook", true);

        sharedPreferences = getSharedPreferences("name_pref", MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();

        // - Set the title for the activity
        setTitle("Update User");

        // - Enable the back button in the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve the user object passed from ViewUserData
        user = getIntent().getParcelableExtra("user");

        // Retrieve the JSON string from SharedPreferences
        String userData = sharedPreferences.getString("user", null);
        // Convert the JSON string back to object using Gson
        Gson gson = new Gson();
        JsonObject userJsonObject = gson.fromJson(userData, JsonObject.class);
        JsonObject nameValuePairsObject = userJsonObject.getAsJsonObject("nameValuePairs");

        // Extract the individual values from the JsonObject
        String id = nameValuePairsObject.get("id").getAsString();
        String userId = nameValuePairsObject.get("user_id").getAsString();
        String username = nameValuePairsObject.get("username").getAsString();
        String password = nameValuePairsObject.get("password").getAsString();
        String role = nameValuePairsObject.get("role").getAsString();
        String email = nameValuePairsObject.get("email").getAsString();
        String birthDay = nameValuePairsObject.get("birthDay").getAsString();
        String birthMonth = nameValuePairsObject.get("birthMonth").getAsString();
        String birthYear = nameValuePairsObject.get("birthYear").getAsString();
        String gender = nameValuePairsObject.get("gender").getAsString();
        String firstName = nameValuePairsObject.get("firstName").getAsString();
        String middleName = nameValuePairsObject.get("middleName").getAsString();
        String lastName = nameValuePairsObject.get("lastName").getAsString();

        // Check if the user object is null
        if (userJsonObject == null) {
            // Display an error message and finish the activity
            Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize the EditText fields
        roleSpinner = findViewById(R.id.roleSpinner);
        daySpinner = findViewById(R.id.daySpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        middleNameEditText = findViewById(R.id.middleNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        userIdEditText = findViewById(R.id.userIdEditText);
        updateButton = findViewById(R.id.updateButton);

        // Set up spinner adapters
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this, R.array.roles_array, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        List<String> daysList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            daysList.add(String.valueOf(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daysList);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        String[] monthNames = new DateFormatSymbols().getMonths();
        List<String> monthsList = new ArrayList<>();
        for (String monthName : monthNames) {
            if (monthName != null && !monthName.isEmpty()) {
                monthsList.add(monthName);
            }
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthsList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        List<String> yearsList = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 100; i--) {
            yearsList.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearsList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        ArrayAdapter<Gender> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Gender.values());
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Populate the EditText fields with the user's current data
        roleSpinner.setSelection(getIndexByValueString(roleSpinner, role));
        emailEditText.setText(email);
        userIdEditText.setText(id);
        usernameEditText.setText(username);
        firstNameEditText.setText(firstName);
        middleNameEditText.setText(middleName);
        lastNameEditText.setText(lastName);

        // - select dat
        String day = birthDay;
        String resultDay = day.substring(day.startsWith("0") ? 1 : 0);
        daySpinner.setSelection(getIndexByValue(daySpinner, String.valueOf(resultDay)));

        // - select month name
        String monthNumber = getMonthName(String.valueOf(birthMonth));
        monthSpinner.setSelection(getIndexByValueString(monthSpinner, monthNumber));

        // - select year
        yearSpinner.setSelection(getIndexByValue(yearSpinner, birthYear));

        // - select gender
        genderSpinner.setSelection(getIndexByValueString(genderSpinner, gender));

        // - Set click listener for the update button
        updateButton.setOnClickListener(view -> {
            updateUser();
        });
    }

    private void updateUser() {
        LoadingDialog loadingDialog = new LoadingDialog(UpdateUserDataActivity.this);
        loadingDialog.startLoadingDialog();

        // Get the updated user data from the EditText fields
        String updatedRole = roleSpinner.getSelectedItem().toString();
        String updatedDay = daySpinner.getSelectedItem().toString();
        String updatedMonth = String.valueOf(monthSpinner.getSelectedItemPosition() + 1);
        String updatedYear = yearSpinner.getSelectedItem().toString();
        Gender updatedGender = (Gender) genderSpinner.getSelectedItem();
        String updatedEmail = emailEditText.getText().toString().trim();
        String updatedUsername = usernameEditText.getText().toString().trim();
        String updatedPassword = passwordEditText.getText().toString().trim();
        String updatedFirstName = firstNameEditText.getText().toString().trim();
        String updatedMiddleName = middleNameEditText.getText().toString().trim();
        String updatedLastName = lastNameEditText.getText().toString().trim();

        // Create an updated User object
        User updatedUser = new User(
                sharedPreferences.getString("user_id", ""),
                updatedUsername,
                updatedPassword,
                updatedRole,
                updatedDay,
                updatedMonth,
                updatedYear,
                updatedGender,
                updatedEmail,
                updatedFirstName,
                updatedMiddleName,
                updatedLastName
        );

        // Call the ResApiHelper method to update the user
        ResApiHelper.updateUserData(this, updatedUser, loadingDialog, new ResApiHelper.commonCallback() {
            @Override
            public void onSuccess() {
                // User data updated successfully
                Toast.makeText(UpdateUserDataActivity.this, "User data updated successfully.", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(UpdateUserDataActivity.this, ViewUserDataActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the failure scenario
                Toast.makeText(UpdateUserDataActivity.this, "Error updating user data: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Define a method to convert month numbers to names
    private String getMonthName(String  month) {
        switch (month) {
            case "01":
                return "January";
            case "02":
                return "February";
            case "03":
                return "March";
            case "04":
                return "April";
            case "05":
                return "May";
            case "06":
                return "June";
            case "07":
                return "July";
            case "08":
                return "August";
            case "09":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                return ""; // Return an empty string for an invalid month
        }
    }

    // Define a method to get the index of a spinner item by its value (String)
    private int getIndexByValueString(Spinner spinner, String value) {
        if (value != null) {
            for (int i = 0; i < spinner.getCount(); i++) {
                String item = spinner.getItemAtPosition(i).toString();
                if (value.equalsIgnoreCase(item)) {
                    return i;
                }
            }
        }
        return 0; // Default to the first item if the value is not found
    }

    // Define a method to get the index of a spinner item by its value (String)
    private int getIndexByValue(Spinner spinner, String value) {
        if (value != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                String item = adapter.getItem(i);
                if (value.equalsIgnoreCase(item)) {
                    return i;
                }
            }
        }
        return 0; // Default to the first item if the value is not found
    }

    // Handle the back button click event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupHeader(boolean backBtn, String title, boolean hamburgerMenu) {
        TextView headerTitle = findViewById(R.id.header_title);
        ImageView btnBack = findViewById(R.id.btn_back);
        ImageView btnHamburger = findViewById(R.id.btn_hamburger);
        btnBack.setVisibility(View.GONE);
        btnHamburger.setVisibility(View.GONE);

        if (hamburgerMenu) {
            btnHamburger.setVisibility(View.VISIBLE);
            btnHamburger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenuFragment();
                }
            });
        }

        if (backBtn) {
            btnBack.setVisibility(View.VISIBLE);

            // - go back
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        headerTitle.setText(title);
    }

    private void showMenuFragment() {
        menuFragment = new MenuFragment(this);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, menuFragment)
                .addToBackStack(null)
                .commit();
    }
}
