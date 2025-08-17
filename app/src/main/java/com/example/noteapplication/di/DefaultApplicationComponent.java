package com.example.noteapplication.di;

import android.content.Context;
import com.example.noteapplication.data.database.NoteDatabase;
import com.example.noteapplication.data.repository.DefaultNoteRepository;
import com.example.noteapplication.data.notification.DefaultNotificationProvider;
import com.example.noteapplication.data.repository.DefaultNotificationRepository;
import com.example.noteapplication.domain.repository.NoteRepository;
import com.example.noteapplication.domain.notification.NotificationProvider;
import com.example.noteapplication.domain.repository.NotificationRepository;

public class DefaultApplicationComponent implements ApplicationComponent {
    private final NoteRepository noteRepository;
    private final NotificationProvider notificationProvider;
    private final NotificationRepository notificationRepository;

    public DefaultApplicationComponent(Context context) {
        noteRepository = new DefaultNoteRepository(
                NoteDatabase.getDatabase(context).getDao(),
                NoteDatabase.getDatabaseExecutor()
        );

        notificationProvider = new DefaultNotificationProvider(context);

        notificationRepository = new DefaultNotificationRepository(context, noteRepository);
    }

    @Override
    public NoteRepository getNoteRepository() {
        return noteRepository;
    }

    @Override
    public NotificationProvider getNotificationProvider() {
        return notificationProvider;
    }

    @Override
    public NotificationRepository getNotificationRepository() {
        return notificationRepository;
    }
}
