package com.example.standup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String toastMessage;
                if (b) {
                    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                            AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
                    toastMessage = "Alarm On";
                } else {
                    if (alarmManager != null) {
                        alarmManager.cancel(pendingIntent);
                    }
                    mNotificationManager.cancelAll();
                    toastMessage = "Alarm Off";
                }
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });


        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggle.setChecked(alarmUp);

        createNotificationChannel();
    }

    public void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mNotificationChannel =
                    new NotificationChannel(PRIMARY_CHANNEL_ID, "Stand up notification", NotificationManager.IMPORTANCE_HIGH);
            mNotificationChannel.enableVibration(true);
            mNotificationChannel.enableVibration(true);
            mNotificationChannel.setLightColor(Color.RED);
            mNotificationChannel.setDescription("Notifies every 15 minutes to stand up and walk");

            mNotificationManager.createNotificationChannel(mNotificationChannel);
        }
    }

}