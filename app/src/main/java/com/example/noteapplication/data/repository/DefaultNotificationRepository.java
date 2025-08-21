package com.example.noteapplication.data.repository;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.notification.NotificationReceiver;
import com.example.noteapplication.domain.repository.NoteRepository;
import com.example.noteapplication.domain.repository.NotificationRepository;

import javax.inject.Inject;

public class DefaultNotificationRepository implements NotificationRepository {

    private final Context context;
    private final AlarmManager alarmManager;
    private final NoteRepository noteRepository;

    @Inject
    public DefaultNotificationRepository(Context context, NoteRepository noteRepository) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);;
        this.noteRepository = noteRepository;
    }

    @Override
    public void create(Note note) {
        if (note.notificationDate == 0L) return;

        PendingIntent pendingIntent = getPendingIntentForAlarmManager(note);
        if (alarmManager != null && pendingIntent != null)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, note.notificationDate, pendingIntent);
    }

    @Override
    public void remove(Note note) {
        if (note.notificationDate == 0L) return;

        PendingIntent pendingIntent = getPendingIntentForAlarmManager(note);
        if (alarmManager != null && pendingIntent != null)
            alarmManager.cancel(pendingIntent);

        Note noteToBeUpdated = noteRepository.getById(note.id);
        noteToBeUpdated.notificationDate = 0L;
        noteRepository.insert(noteToBeUpdated);
    }

    private PendingIntent getPendingIntentForAlarmManager(Note note) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.NOTE_ID_KEY, note.id);
        return PendingIntent.getBroadcast(context, note.id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
