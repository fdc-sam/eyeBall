package live.sai.eyeball.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import live.sai.eyeball.Model.User;
import live.sai.eyeball.R;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;



public class VideoCallFragment extends Fragment {
    private User user;
    private View rootView;
    private View fragmentRootView;
    private Button btnDelete, hideButton, alertVideoCallBtn;

    public void setUser(User user) {
        this.user = user;
    }

    ZegoSendCallInvitationButton videoCallBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video_call, container, false);

        alertVideoCallBtn = rootView.findViewById(R.id.alert_btn_video_call);
        alertVideoCallBtn.setOnClickListener(buttonClickListener);

        videoCallBtn = rootView.findViewById(R.id.video_call_btn);

        hideButton = rootView.findViewById(R.id.alert_btn_hide_video_call_fragment);
        hideButton.setOnClickListener(buttonClickListener);

        if (user != null) {
            String user_id = user.getUserId();
            Log.d("user_idM", user_id);
            setVideoCall(user_id);
            // - Hide the fragment's view at startup
            rootView.setVisibility(View.VISIBLE);
        } else {
            // - Hide the fragment's view at startup
            rootView.setVisibility(View.GONE);
        }

        return rootView;
    }

    void setVideoCall(String user_id) {
        videoCallBtn.setIsVideoCall(true);
        videoCallBtn.setResourceID("zego_uikit_call");
        videoCallBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(user_id)));
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.alert_btn_hide_video_call_fragment) {
                // - Handle the hideButton click event
                rootView.setVisibility(View.GONE);
            } else if (v.getId() == R.id.alert_btn_video_call) {
                videoCallBtn.performClick();
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
