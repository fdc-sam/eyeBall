package live.sai.eyeball.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import live.sai.eyeball.Activity.SigninActivity;
import live.sai.eyeball.Activity.SignupActivity;
import live.sai.eyeball.Activity.UpdateUserDataActivity;
import live.sai.eyeball.Activity.ViewUserDataActivity;
import live.sai.eyeball.R;

public class MenuFragment extends Fragment {
    private ImageView alertBtnClose;
    private View rootView;

    private Button alertBtnViewProfile, alertBtnLogout;
    private AppCompatActivity activity;

    private ViewUserDataActivity viewUserDataActivity;
    private SignupActivity signupActivity;
    private UpdateUserDataActivity updateUserDataActivity;

    public MenuFragment(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        alertBtnViewProfile = rootView.findViewById(R.id.alert_btn_view_profile);
        alertBtnViewProfile.setOnClickListener(buttonClickListener);

        alertBtnLogout = rootView.findViewById(R.id.alert_btn_logout);
        alertBtnLogout.setOnClickListener(buttonClickListener);

        alertBtnClose = rootView.findViewById(R.id.alert_btn_close);
        alertBtnClose.setOnClickListener(buttonClickListener);

        // Inflate the layout for this fragment
        return rootView;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.alert_btn_view_profile) {
                // Handle the alert_btn_view_profile click event
                openUpdateUserDataActivity();
            } else if (v.getId() == R.id.alert_btn_logout) {
                // Handle the alert_btn_logout click event
                logoutAndNavigateToSignInActivity();
            } else if (v.getId() == R.id.alert_btn_close) {
                // Handle the hideButton click event
                hideCurrentFragment();
            }
        }
    };

    private void openUpdateUserDataActivity() {
        Intent intent = new Intent(activity, UpdateUserDataActivity.class);
        startActivity(intent);
    }

    private void logoutAndNavigateToSignInActivity() {
        Intent intent = new Intent(activity, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    private void hideCurrentFragment() {
        if (getParentFragmentManager() != null) {
            Fragment currentFragment = getParentFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment != null) {
                getParentFragmentManager().beginTransaction().remove(currentFragment).commit();
            }
        }
    }
}
