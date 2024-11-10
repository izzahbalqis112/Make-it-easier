package ftmk.bitp3453.authenticationapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Locale;

import ftmk.bitp3453.authenticationapp.R;

public class TimerActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILIS = 1500000;

    private ProgressBar progressBar;
    private ImageView btnBack;
    private Button buttonStart, buttonReset;
    private TextView textViewTimer,username;
    private CheckBox checkBoxAutoSetTimer;

    Switch buttonSwitch;

    private CountDownTimer countDownTimer;
    private  boolean timerRunning;
    private boolean timerStopping;

    private long timeLeftMilis = START_TIME_IN_MILIS;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    BottomNavigationView navigationView;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimerActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });
        buttonStart = findViewById(R.id.btnStart);
        buttonReset = findViewById(R.id.btnReset);
        textViewTimer = findViewById(R.id.tvCountDown);
        progressBar = findViewById(R.id.progressBar2);

        buttonSwitch = findViewById(R.id.switch1);
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// Check if the notification policy access has been granted for the app.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!notificationManager.isNotificationPolicyAccessGranted()) {
                        Intent intent = new
                                Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(intent);
                        return;
                    }
                }
                buttonSwitch(notificationManager);
            }
        });


        buttonReset.setVisibility(View.INVISIBLE);

        navigationView = findViewById(R.id.bottomNavigation);
        navigationView.setSelectedItemId(R.id.home);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.subject:
                        //startActivity(new Intent(getApplicationContext(),TaskActivity.class));
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.meeting:
                        startActivity(new Intent(getApplicationContext(),MeetingActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();



        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }


        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                resetTimer();
                progressBar.setProgress(1500000);
            }
        });

        updateCountDownText();

    }


    private void startTimer()
    {
        countDownTimer = new CountDownTimer(timeLeftMilis,1000) {
            @Override
            public void onTick(long msUntilFinished) {
                timeLeftMilis = msUntilFinished;
                updateCountDownText();
                progressBar.setMax((int)START_TIME_IN_MILIS);
                progressBar.setProgress((int)msUntilFinished);

            }

            @Override
            public void onFinish() {
                timerRunning = false;
                buttonStart.setText("Start");
                buttonStart.setVisibility(View.INVISIBLE);
                buttonReset.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);


                if(checkBoxAutoSetTimer.isChecked())
                {
                    updateCountDownText();
                }

            }
        }.start();

        timerRunning = true;
        buttonStart.setText("Pause");
        buttonStart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.timer_pause, 0, 0, 0);
        buttonReset.setVisibility(View.INVISIBLE);

    }

    private void pauseTimer()
    {
        countDownTimer.cancel();
        timerRunning = false;
        buttonStart.setText("Resume");
        buttonStart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play_timer, 0, 0, 0);
        buttonReset.setVisibility(View.VISIBLE);

    }

    private void resetTimer()
    {
        timeLeftMilis = START_TIME_IN_MILIS;
        updateCountDownText();
        buttonReset.setVisibility(View.INVISIBLE);
        buttonStart.setVisibility(View.VISIBLE);
        buttonStart.setText("Start To Focus");
        buttonStart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play_timer, 0, 0, 0);



    }

    private void updateCountDownText()
    {

        int minutes = (int) (timeLeftMilis/1000)/60;
        int seconds = (int) (timeLeftMilis/1000)%60;
        int hours = 0;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
        textViewTimer.setText(timeLeftFormatted);

    }

    private void buttonSwitch(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (notificationManager.getCurrentInterruptionFilter() == NotificationManager.INTERRUPTION_FILTER_ALL) {
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
                //audioToggle.setText(R.string.fa_volume_mute);
            } else {
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                //audioToggle.setText(R.string.fa_volume_up);
            }
        }
    }
}