package live.sai.eyeball.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import live.sai.eyeball.Adapter.UserDataAdapter;
import live.sai.eyeball.Fragment.ViewUserDetailFragment;
import live.sai.eyeball.Global.BaseActivity;
import live.sai.eyeball.Loader.LoadingDialog;
import live.sai.eyeball.Model.User;
import live.sai.eyeball.R;
import live.sai.eyeball.RestApiHelper.ResApiHelper;
import live.sai.eyeball.SingleTon.UserDataManager;

import com.zegocloud.uikit.plugin.signaling.ZegoSignalingPlugin;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig;
import com.zegocloud.uikit.prebuilt.call.config.ZegoHangUpConfirmDialogInfo;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import androidx.fragment.app.FragmentManager;
import live.sai.eyeball.Fragment.MenuFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ViewUserDataActivity extends BaseActivity implements UserDataAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private UserDataAdapter adapter;
    private List<User> userList;
    private LoadingDialog loadingDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentManager fragmentManager;
    private String userID, username;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_data);

        // Set the header for the activity
        setupHeader(false, "handbook", true);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        userList = new ArrayList<>();
        loadingDialog = new LoadingDialog(ViewUserDataActivity.this);

        // Check if the fragment is not null and currently visible
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ViewUserDetailFragment())
                    .commit();
        }
        setupRecyclerView();

        fragmentManager = getSupportFragmentManager();

        // Set up the SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Fetch user data
                fetchUserData();
            }
        });

        // Fetch user data initially
        fetchUserData();

        userID = UserDataManager.getInstance().getId();
        username = UserDataManager.getInstance().getUsername();
        // - set up video sdk
        startService(userID, username);
    }

    void startService(String user_id, String user_name) {
        long appID = 286072669;   // yourAppID
        String appSign = "66990e398f0f5c9afac82c2ded223a505e0686462baa640a9d9c2b2957d6d445";
        String userID = user_id; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName = user_name;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
        callInvitationConfig.innerText.incomingCallPageDeclineButton = "Decline";
        callInvitationConfig.innerText.incomingCallPageAcceptButton = "Accept";

        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName, callInvitationConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserDataAdapter(this, userList, loadingDialog, getSupportFragmentManager());
        // adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void fetchUserData() {
        loadingDialog.startLoadingDialog();

        // Call the API to fetch user data
        ResApiHelper.getUserData(ViewUserDataActivity.this, new ResApiHelper.UserDataCallback() {
            @Override
            public void onSuccess(List<User> data) {
                try {
                    userList.clear();
                    userList.addAll(data);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("ViewActivity", "Exception: " + e.getMessage());
                } finally {
                    // After refreshing the data, call setRefreshing(false) to stop the loading animation
                    swipeRefreshLayout.setRefreshing(false);
                    loadingDialog.dismissDialog();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // After refreshing the data, call setRefreshing(false) to stop the loading animation
                swipeRefreshLayout.setRefreshing(false);
                loadingDialog.dismissDialog();
                Log.e("ViewActivity", "Failed to fetch user data: " + errorMessage);
                // Show an error message or handle the failure case
            }
        });
    }

    @Override
    public void onItemClick(User user) {
        // Handle item click here
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
