package live.sai.eyeball.Global;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import live.sai.eyeball.Activity.SigninActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private static final int BACK_PRESS_DELAY = 2000; // Delay for double back press in milliseconds
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            showExitConfirmationDialog();
        } else {
            this.doubleBackToExitPressedOnce = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, BACK_PRESS_DELAY);
        }
    }

    protected void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                closeApp();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void closeApp() {
        finishAffinity(); // Finish the current activity and all activities in the stack
        System.exit(0); // Close the app process
    }
}
