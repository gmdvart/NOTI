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

public final class NoteNotificationManager {
    private static final String CHANNEL_ID = "NOTE_NOTIFICATION_CHANNEL_ID";
    private static final String CHANNEL_NAME = "NOTE_NOTIFICATION_CHANNEL";
    private static final String CHANNEL_DESCRIPTION = "Note Reminder Channel";
    private static final int NOTIFICATION_ID = 2281337;

    private final Context context;

    NoteNotificationManager(final Context context) {
        this.context = context;
    }

    public void createNotification(String title, String description) {
        createNotificationChannel();

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setLargeIcon(icon)
                .setContentTitle("Time to check your Note!")
                .setContentText("Some text...")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission is not granted", Toast.LENGTH_SHORT).show();
            return;
        }
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, android.app.NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(CHANNEL_DESCRIPTION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}
