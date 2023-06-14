package live.sai.eyeball.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import live.sai.eyeball.Fragment.MenuFragment;
import live.sai.eyeball.Inum.Gender;
import live.sai.eyeball.Loader.LoadingDialog;
import live.sai.eyeball.R;
import live.sai.eyeball.RestApiHelper.ResApiHelper;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignupActivity extends AppCompatActivity {

    private Spinner roleSpinner, daySpinner, monthSpinner, yearSpinner, genderSpinner;
    private EditText emailEditText, usernameEditText, passwordEditText, firstNameEditText, middleNameEditText, lastNameEditText;
    private Button signupButton;
    private FragmentManager fragmentManager;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final LoadingDialog loadingDialog = new LoadingDialog(SignupActivity.this);

        // Set up UI elements
        setupUI();

        // Set up spinner adapters
        setupSpinners();

        // Set up signup button click listener
        setupSignupButton(loadingDialog);

        fragmentManager = getSupportFragmentManager();
        menuFragment = new MenuFragment(SignupActivity.this);
    }

    private void setupUI() {
        // Set the header for the activity
        setupHeader(true, "Signup", false);

        // Enable the back button in the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize UI elements
        roleSpinner = findViewById(R.id.roleSpinner);
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        middleNameEditText = findViewById(R.id.middleNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        daySpinner = findViewById(R.id.daySpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);
        signupButton = findViewById(R.id.signupButton);
    }

    private void setupSpinners() {
        // Set up spinner adapters
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this, R.array.roles_array, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        // Set up day spinner with dynamically generated days
        List<String> daysList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            daysList.add(String.valueOf(i));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daysList);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // Set up month spinner with dynamically generated month names
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

        // Set up year spinner with dynamically generated years
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
    }

    private void setupSignupButton(final LoadingDialog loadingDialog) {
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the selected values from the spinners
                String selectedRole = roleSpinner.getSelectedItem().toString();
                String selectedDay = daySpinner.getSelectedItem().toString();
                String selectedMonth = monthSpinner.getSelectedItem().toString();
                String selectedYear = yearSpinner.getSelectedItem().toString();
                Gender selectedGender = (Gender) genderSpinner.getSelectedItem();

                // Retrieve the entered text from the edit text fields
                String email = emailEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String middleName = middleNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();

                // Perform signup logic here
                loadingDialog.startLoadingDialog();
                ResApiHelper.addUser(
                        SignupActivity.this,
                        username,
                        password,
                        selectedRole,
                        selectedDay,
                        selectedMonth,
                        selectedYear,
                        selectedGender,
                        email,
                        firstName,
                        middleName,
                        lastName,
                        loadingDialog
                );
            }
        });
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
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, menuFragment)
                .addToBackStack(null)
                .commit();
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
}
