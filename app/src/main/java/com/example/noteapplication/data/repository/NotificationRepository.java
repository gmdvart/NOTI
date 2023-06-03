package com.example.noteapplication.data.repository;

import android.content.Context;
import androidx.work.*;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.worker.NotificationWorker;
import com.example.noteapplication.utils.NoteGsonHelper;

import java.util.concurrent.TimeUnit;

public class NotificationRepository {
    private final WorkManager workManager;

    public NotificationRepository(final Context context) {
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
