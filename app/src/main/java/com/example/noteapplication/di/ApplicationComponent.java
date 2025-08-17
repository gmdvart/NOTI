package com.example.noteapplication.di;

import com.example.noteapplication.domain.notification.NotificationProvider;
import com.example.noteapplication.domain.repository.NoteRepository;
import com.example.noteapplication.domain.repository.NotificationRepository;

public interface ApplicationComponent {
    NoteRepository getNoteRepository();
    NotificationProvider getNotificationProvider();
    NotificationRepository getNotificationRepository();
}
