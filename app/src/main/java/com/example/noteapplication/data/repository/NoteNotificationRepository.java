package com.example.noteapplication.data.repository;

import android.content.Context;
import android.util.Log;
import androidx.work.*;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.worker.NotificationWorker;
import com.example.noteapplication.utils.NoteGsonHelper;

import java.util.concurrent.TimeUnit;

public class NoteNotificationRepository {
    private final WorkManager workManager;

    public NoteNotificationRepository(final Context context) {
        this.workManager = WorkManager.getInstance(context);
    }

    public void makeNotification(Note note) {
        Data inputData = createInputDataForNotification(note);
        String workTag = Integer.toString(note.id);
        int timeDiff = note.notificationDate - note.creationDate;

        OneTimeWorkRequest notificationRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(inputData)
                .addTag(workTag)
                .setInitialDelay(timeDiff, TimeUnit.SECONDS)
                .build();

        workManager.enqueue(notificationRequest);
    }

    private Data createInputDataForNotification(Note note) {
        String noteJson = NoteGsonHelper.noteToJson(note);

        return new Data.Builder()
                .putString(NotificationWorker.NOTE_JSON_KEY, noteJson)
                .build();
    }
}
