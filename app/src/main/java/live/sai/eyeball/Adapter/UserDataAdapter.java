package live.sai.eyeball.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import live.sai.eyeball.Activity.UpdateUserDataActivity;
import live.sai.eyeball.Fragment.ViewUserDetailFragment;
import live.sai.eyeball.Fragment.VoiceCallFragment;
import live.sai.eyeball.Fragment.VideoCallFragment;

import live.sai.eyeball.Loader.LoadingDialog;
import live.sai.eyeball.Model.User;
import live.sai.eyeball.R;
import live.sai.eyeball.Util.DateConverter;

import java.util.List;
import java.util.Random;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {

    private List<User> dataList;
    private Context context;
    private LoadingDialog loadingDialog;
    private FragmentManager fragmentManager;

    public UserDataAdapter(Context context, List<User> dataList, LoadingDialog loadingDialog, FragmentManager fragmentManager) {
        this.context = context;
        this.dataList = dataList;
        this.loadingDialog = loadingDialog;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = dataList.get(position);
        holder.nameTextView.setText(user.getFirstName() + " " + user.getLastName());
        holder.genderTextView.setText(user.getGender() != null ? user.getGender().toString() : "");
        holder.birthTextView.setText(DateConverter.convertToWordsDate(user.getSelectedDay() + "/" + user.getSelectedMonth() + "/" + user.getSelectedYear()));
        holder.emailTextView.setText(user.getEmail());
        holder.idTextView.setText(user.getUserId());

        // Get the first letter of the name
        char firstLetter = Character.toUpperCase(user.getFirstName().charAt(0));

        // Set the first letter to the TextView
        holder.firstLetterName.setText(String.valueOf(firstLetter));

        RelativeLayout relativeLayoutFirstName = holder.itemView.findViewById(R.id.relative_layout_first_name);

        // Generate a random color
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        // Get the background drawable of the firstLetterName TextView
        GradientDrawable backgroundDrawable = (GradientDrawable) relativeLayoutFirstName.getBackground();

        // Set the random color to the background drawable
        backgroundDrawable.setColor(color);


        // - Handle voice call click event
        holder.btnShowVoiceCallAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // - Handle voice call button click
                openVoiceCall(user);
            }
        });

        // - Handle video call click event
        holder.btnShowVideoCallAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // - Handle video call button click
                openVideoCall(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTextView, genderTextView, birthTextView, emailTextView, idTextView, itemTextView, firstLetterName;
        public Button btn_delete, btnShowVideoCallAlert, btnShowVoiceCallAlert;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            birthTextView = itemView.findViewById(R.id.birthTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            idTextView = itemView.findViewById(R.id.idTextView);
            itemTextView = itemView.findViewById(R.id.itemTextView);

            firstLetterName = itemView.findViewById(R.id.first_letter_name);

            btnShowVideoCallAlert = itemView.findViewById(R.id.btn_show_video_call_alert);
            btnShowVoiceCallAlert = itemView.findViewById(R.id.btn_show_voice_call_alert);

            btn_delete = itemView.findViewById(R.id.btn_delete);

            // Set click listener on item view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                User user = dataList.get(position);
                openViewUserDetailFragment(user);
            }
        }
    }

    private void openViewUserDetailFragment(User user) {
        ViewUserDetailFragment viewUserDetailFragment = new ViewUserDetailFragment();
        // Set the user data in the viewUserDetailFragment
        viewUserDetailFragment.setUser(user);

        // Replace the current fragment with viewUserDetailFragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, viewUserDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openVoiceCall(User user){
        VoiceCallFragment voiceCallFragment = new VoiceCallFragment();
        // Set the user data in the voiceCallFragment
        voiceCallFragment.setUser(user);

        if (user != null){
            // Replace the current fragment with voiceCallFragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, voiceCallFragment)
                    .addToBackStack(null)
                    .commit();

            Log.d("userId: ", user.getUserId());
        }
    }

    private void openVideoCall(User user){
        VideoCallFragment videoCallFragment = new VideoCallFragment();
        // Set the user data in the videoCallFragment
        videoCallFragment.setUser(user);

        if (user != null){
            // Replace the current fragment with videoCallFragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, videoCallFragment)
                    .addToBackStack(null)
                    .commit();

            Log.d("userId: ", user.getUserId());
        }
    }
}
