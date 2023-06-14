package live.sai.eyeball.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import live.sai.eyeball.Activity.UpdateUserDataActivity;
import live.sai.eyeball.Loader.LoadingDialog;
import live.sai.eyeball.Model.User;
import live.sai.eyeball.R;
import live.sai.eyeball.RestApiHelper.ResApiHelper;
import com.google.android.material.snackbar.Snackbar;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

public class ViewUserDetailFragment extends Fragment {
    private User user;
    private View rootView;
    private Button hideButton, btnUpdate, alertVoiceCallBtn, alertVideoCallBtn;

    public void setUser(User user) {
        this.user = user;
    }

    ZegoSendCallInvitationButton voiceCallBtn, videoCallBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_user_detail, container, false);

        // Set the header for the activity
        setupHeader(true, "handbook", false);

        TextView nameTextView = rootView.findViewById(R.id.nameTextView);
        TextView genderTextView = rootView.findViewById(R.id.genderTextView);
        TextView birthTextView = rootView.findViewById(R.id.birthTextView);
        TextView emailTextView = rootView.findViewById(R.id.emailTextView);
        TextView idTextView = rootView.findViewById(R.id.idTextView);

        alertVoiceCallBtn = rootView.findViewById(R.id.alert_btn_voice_call);
        if (alertVoiceCallBtn != null) {
            alertVoiceCallBtn.setOnClickListener(buttonClickListener);
        }

        alertVideoCallBtn = rootView.findViewById(R.id.alert_btn_video_call);
        if (alertVideoCallBtn != null) {
            alertVideoCallBtn.setOnClickListener(buttonClickListener);
        }

        hideButton = rootView.findViewById(R.id.alert_btn_hide_voice_call_fragment);
        if (hideButton != null) {
            hideButton.setOnClickListener(buttonClickListener);
        }

        voiceCallBtn = rootView.findViewById(R.id.voice_call_btn);
        videoCallBtn = rootView.findViewById(R.id.video_call_btn);

        if (user != null) {
            nameTextView.setText(user.getFirstName() + " " + user.getLastName());
            // - Handle gender text view
            if (user.getGender() != null) {
                genderTextView.setText(user.getGender().toString());
            } else {
                genderTextView.setText("");
            }
            String user_id = user.getUserId();
            Log.d("user_id", user_id);
            setVideoCall(user_id);
            setVoiceCall(user_id);

            birthTextView.setText(user.getDateCreate());
            emailTextView.setText(user.getEmail());
            idTextView.setText(user.getUserId());
            // - Hide the fragment's view at startup
            rootView.setVisibility(View.VISIBLE);
        } else {
            // - Hide the fragment's view at startup
            rootView.setVisibility(View.GONE);
        }

        return rootView;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_delete) {
                Log.d("User : ", user.getUserId());
                LoadingDialog loadingDialog = new LoadingDialog(requireActivity());
                deleteUser(user, loadingDialog);
            } else if (v.getId() == R.id.alert_btn_voice_call) {
                voiceCallBtn.performClick();
            } else if (v.getId() == R.id.alert_btn_video_call) {
                videoCallBtn.performClick();
            } else if (v.getId() == R.id.alert_btn_hide_voice_call_fragment) {
                rootView.setVisibility(View.GONE); // Hide the fragment's view
            }
        }
    };

    private void setupHeader(boolean backBtn, String title, boolean hamburgerMenu) {
        TextView headerTitle = rootView.findViewById(R.id.header_title);
        ImageView btnBack = rootView.findViewById(R.id.btn_back);
        ImageView btnHamburger = rootView.findViewById(R.id.btn_hamburger);
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
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rootView.setVisibility(View.GONE); // Hide the fragment's view
                }
            });
        }

        headerTitle.setText(title);
    }

    private void showMenuFragment() {
        // Implement the logic to show the menu fragment
    }

    void setVideoCall(String user_id) {
        videoCallBtn.setIsVideoCall(true);
        videoCallBtn.setResourceID("zego_uikit_call");
        videoCallBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(user_id)));
    }

    void setVoiceCall(String user_id) {
        voiceCallBtn.setIsVideoCall(false);
        voiceCallBtn.setResourceID("zego_uikit_call");
        voiceCallBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(user_id)));
    }

    private void deleteUser(User user, LoadingDialog loadingDialog) {
        // Create and show a message dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete this user?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadingDialog.startLoadingDialog();
                // Perform delete operation using ResApiHelper or any other necessary actions
                ResApiHelper.deleteUserData(requireContext(), user, loadingDialog, new ResApiHelper.commonCallback() {
                    public void onSuccess() {
                        // User data deleted successfully
                        Snackbar.make(requireActivity().findViewById(android.R.id.content), "User data deleted successfully.", Snackbar.LENGTH_SHORT).show();
                        // Handle any other UI updates or actions after successful deletion
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle the failure scenario
                        Snackbar.make(requireActivity().findViewById(android.R.id.content), "Error deleting user data: " + errorMessage, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
