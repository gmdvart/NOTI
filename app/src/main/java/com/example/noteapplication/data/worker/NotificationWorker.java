package com.example.noteapplication.data.worker;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.noteapplication.NoteApplication;
import com.example.noteapplication.constants.NoteNotificationsKeys;
import com.example.noteapplication.data.app.AppDataContainer;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.repository.NoteRepository;
import com.example.noteapplication.ui.NoteDateSelectionIndexSaver;
import com.example.noteapplication.utils.NoteGsonHelper;
import com.example.noteapplication.utils.NoteNotificationManager;

public class NotificationWorker extends Worker {
    public static final String NOTE_JSON_KEY = "note_json";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String noteJson = getInputData().getString(NOTE_JSON_KEY);

        Note note = NoteGsonHelper.jsonToNote(noteJson);

        String title, description;
        if (note.title == null) title = "";
        else title = note.title;
        if (note.description == null) description = "";
        else description = note.description;

        NoteNotificationManager notificationManager = new NoteNotificationManager(getApplicationContext());
        notificationManager.createNotification(title, description);

        resetNoteNotification(note);

        return Result.success();
    }

    private void resetNoteNotification(Note note) {
        NoteApplication application = (NoteApplication) getApplicationContext();
        AppDataContainer container = application.getContainer();
        NoteRepository repository = container.getNoteRepository();

        note.notificationDate = NoteNotificationsKeys.WITHOUT_NOTIFICATION;
        note.indices = new NoteDateSelectionIndexSaver(0, 0, 0);

        repository.updateNote(note);
    }
}
