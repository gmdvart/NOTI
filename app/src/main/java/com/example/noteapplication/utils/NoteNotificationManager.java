package com.example.noteapplication.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.noteapplication.MainActivity;
import com.example.noteapplication.R;

import java.util.Calendar;
import java.util.Random;

public final class NoteNotificationManager {
    private static final String CHANNEL_ID = "NOTE_NOTIFICATION_CHANNEL_ID";
    private static final String CHANNEL_NAME = "NOTI Notifications";
    private static final String CHANNEL_DESCRIPTION = "Note Reminder Channel";

    private final Context context;

    public NoteNotificationManager(final Context context) {
        this.context = context;
    }

    public void createNotification(String title, String description) {
        createNotificationChannel();

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Post Notifications is not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        long seed = Calendar.getInstance().getTimeInMillis();
        Random random = new Random(seed);
        int channelId = random.nextInt();

        NotificationManagerCompat.from(context).notify(channelId, notification);
    }

    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, android.app.NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}
