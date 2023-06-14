package live.sai.eyeball.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import live.sai.eyeball.Loader.LoadingDialog;
import live.sai.eyeball.R;
import live.sai.eyeball.RestApiHelper.Login;
import live.sai.eyeball.RestApiHelper.Login.LoadingDialogCallback;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SigninActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button loginButton, signupButton;
    private CheckBox rememberMeCheckBox;

    private TextInputLayout textInputLayout;
    private TextInputEditText passwordEditText;

    // - declare shared preferences
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // - Set the title for the activity
        setTitle("Sign-in");

        sharedPreferences = getSharedPreferences("name_pref", MODE_PRIVATE);

        // Find the eye icon ImageView
        textInputLayout = findViewById(R.id.passwordTextInputLayout);

        textInputLayout = findViewById(R.id.passwordTextInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);

        // - set preferences
        usernameEditText.setText(sharedPreferences.getString("username", ""));
        passwordEditText.setText(sharedPreferences.getString("password", ""));
        rememberMeCheckBox.setChecked(sharedPreferences.getBoolean("remember_me", false));

        final LoadingDialog loadingDialog = new LoadingDialog(SigninActivity.this);

        // - check

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                loadingDialog.startLoadingDialog();
                Login.login(SigninActivity.this, username, password, new LoadingDialogCallback() {
                    @Override
                    public void onDismiss() {
                        loadingDialog.dismissDialog();
                    }
                });

                // Save the "Remember Me" preference
                if (rememberMeCheckBox.isChecked()) {
                    sharedPreferences.edit().putBoolean("remember_me", true).apply();
                } else {
                    sharedPreferences.edit().remove("remember_me").apply();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        // Listen for the Enter key press event on the password EditText
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Perform login when the Enter key is pressed
                    String username = usernameEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    loadingDialog.startLoadingDialog();
                    Login.login(SigninActivity.this, username, password, new LoadingDialogCallback() {
                        @Override
                        public void onDismiss() {
                            loadingDialog.dismissDialog();
                        }
                    });

                    // Save the "Remember Me" preference
                    if (rememberMeCheckBox.isChecked()) {
                        sharedPreferences.edit().putBoolean("remember_me", true).apply();
                    } else {
                        sharedPreferences.edit().remove("remember_me").apply();
                    }

                    return true;
                }
                return false;
            }
        });
    }

}
