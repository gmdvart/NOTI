package com.example.noteapplication.data.repository;

import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;
import androidx.work.*;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.worker.NotificationWorker;
import com.example.noteapplication.utils.NoteGsonHelper;
import com.example.noteapplication.utils.NoteUtils;

import java.util.concurrent.TimeUnit;

public class NotificationRepository {
    private final WorkManager workManager;

    public NotificationRepository(final Context context) {
        this.workManager = WorkManager.getInstance(context);
    }

    public void makeNotification(Note note) {
        Data inputData = createInputDataForNotification(note);
        String workTag = Integer.toString(note.id);

        long timeDiff = note.notificationDate * 1000L - Calendar.getInstance().getTimeInMillis();

        OneTimeWorkRequest notificationRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(inputData)
                .addTag(workTag)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build();

        workManager.enqueue(notificationRequest);
    }

    public void cancelNotification(int noteId) {
        workManager.cancelAllWorkByTag(Integer.toString(noteId));
    }

    private Data createInputDataForNotification(Note note) {
        String noteJson = NoteGsonHelper.noteToJson(note);

        return new Data.Builder()
                .putString(NotificationWorker.NOTE_JSON_KEY, noteJson)
                .build();
    }
}
