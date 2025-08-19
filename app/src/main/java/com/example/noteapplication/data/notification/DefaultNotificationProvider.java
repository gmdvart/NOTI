package com.example.noteapplication.data.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.noteapplication.R;
import com.example.noteapplication.domain.notification.NotificationProvider;

public class DefaultNotificationProvider implements NotificationProvider {

    private static final String CHANNEL_ID = "com.example.noteapplication.NOTIFICATION_CHANNEL";
    private final Context context;

    public DefaultNotificationProvider(Context context) {
        this.context = context;
    }

    @Override
    public void create(int id, String title, String text) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notifcation_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            NotificationManagerCompat.from(context).notify(id, builder.build());
    }
}
