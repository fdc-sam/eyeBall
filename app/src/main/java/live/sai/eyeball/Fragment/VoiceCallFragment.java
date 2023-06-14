package live.sai.eyeball.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
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
import live.sai.eyeball.SingleTon.UserDataManager;

import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

public class VoiceCallFragment extends Fragment {
    private User user;
    private View rootView;
    private View fragmentRootView;
    private Button btnDelete, hideButton, alertVoiceCallBtn;

    public void setUser(User user) {
        this.user = user;
    }

    ZegoSendCallInvitationButton voiceCallBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_voice_call, container, false);

        alertVoiceCallBtn = rootView.findViewById(R.id.alert_btn_voice_call);
        alertVoiceCallBtn.setOnClickListener(buttonClickListener);

        voiceCallBtn = rootView.findViewById(R.id.voice_call_btn);

        hideButton = rootView.findViewById(R.id.alert_btn_hide_voice_call_fragment);
        hideButton.setOnClickListener(buttonClickListener);

        if (user != null) {
            String user_id = user.getUserId();
            String username = UserDataManager.getInstance().getUsername();


            Log.d("user_idM", user_id);
            setVoiceCall(user_id);
            // - Hide the fragment's view at startup
            rootView.setVisibility(View.VISIBLE);
        } else {
            // - Hide the fragment's view at startup
            rootView.setVisibility(View.GONE);
        }

        return rootView;
    }

    void setVoiceCall(String user_id) {
        voiceCallBtn.setIsVideoCall(false);
        voiceCallBtn.setResourceID("zego_uikit_call");
        voiceCallBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(user_id)));
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.alert_btn_hide_voice_call_fragment) {
                // - Handle the hideButton click event
                rootView.setVisibility(View.GONE);
            } else if (v.getId() == R.id.alert_btn_voice_call) {
                voiceCallBtn.performClick();
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
