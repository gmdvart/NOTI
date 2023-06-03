package com.example.noteapplication.data.app;

import android.content.Context;
import com.example.noteapplication.data.database.NoteDatabase;
import com.example.noteapplication.data.repository.NoteManagementRepository;
import com.example.noteapplication.data.repository.NotificationRepository;
import com.example.noteapplication.data.repository.NoteRepository;

public class AppDataContainer {
    private final NoteRepository noteRepository;
    private final NotificationRepository notificationRepository;

    public AppDataContainer(final Context context) {
        noteRepository = new NoteManagementRepository(
                NoteDatabase.getDatabase(context).getDao(),
                NoteDatabase.getDatabaseExecutor()
        );
        notificationRepository = new NotificationRepository(context);
    }

    public NoteRepository getNoteRepository() {
        return noteRepository;
    }
    public NotificationRepository getNotificationRepository() {
        return notificationRepository;
    }
}
