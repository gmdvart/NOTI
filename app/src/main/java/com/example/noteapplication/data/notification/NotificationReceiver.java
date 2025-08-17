package com.example.noteapplication.data.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.noteapplication.NoteApplication;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.repository.DefaultNoteRepository;
import com.example.noteapplication.di.ApplicationComponent;
import com.example.noteapplication.domain.notification.NotificationProvider;

public class NotificationReceiver extends BroadcastReceiver {

    public final static String ACTION_NOTIFY_USER = "com.example.noteapplication.ACTION_NOTIFY_USER";
    public final static String NOTE_ID_KEY = "com.example.noteapplication.NOTE_ID_KEY";

    @Override
    public void onReceive(@NonNull Context context, @Nullable Intent intent) {
        Log.d(getClass().getSimpleName(), "Received intent");

        if (intent == null) return;

        final PendingResult result = goAsync();

        ApplicationComponent applicationComponent = ((NoteApplication) context.getApplicationContext()).getAppComponent();
        DefaultNoteRepository noteRepository = (DefaultNoteRepository) applicationComponent.getNoteRepository();
        NotificationProvider notificationProvider = applicationComponent.getNotificationProvider();

        // Step 1: Extract note from repository
        int noteId = intent.getIntExtra(NOTE_ID_KEY, -1);
        Note note = noteRepository.getById(noteId);
        // Step 2: Create notification based on note's info
        notificationProvider.create(note.id, note.title, note.description);
        // Step 3: Update notification date to zero so the user will have an opportunity to set notification again
        // ... and update it to repository
        note.notificationDate = 0L;
        noteRepository.insert(note);

        result.finish();
    }
}
