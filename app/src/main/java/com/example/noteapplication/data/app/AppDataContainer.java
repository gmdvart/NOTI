package com.example.noteapplication.data.app;

import android.content.Context;
import com.example.noteapplication.data.database.NoteDatabase;
import com.example.noteapplication.data.repository.NoteManagementRepository;
import com.example.noteapplication.data.repository.NoteNotificationRepository;
import com.example.noteapplication.data.repository.NoteRepository;

public class AppDataContainer {
    private final NoteRepository noteRepository;
    private final NoteNotificationRepository notificationRepository;

    public AppDataContainer(final Context context) {
        noteRepository = new NoteManagementRepository(
                NoteDatabase.getDatabase(context).getDao(),
                NoteDatabase.getDatabaseExecutor()
        );
        notificationRepository = new NoteNotificationRepository(context);
    }

    public NoteRepository getNoteRepository() {
        return noteRepository;
    }
    public NoteNotificationRepository getNotificationRepository() {
        return notificationRepository;
    }
}
