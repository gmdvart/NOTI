package com.example.noteapplication.di.modules;

import com.example.noteapplication.data.notification.DefaultNotificationProvider;
import com.example.noteapplication.data.repository.DefaultNoteRepository;
import com.example.noteapplication.data.repository.DefaultNotificationRepository;
import com.example.noteapplication.domain.notification.NotificationProvider;
import com.example.noteapplication.domain.repository.NoteRepository;
import com.example.noteapplication.domain.repository.NotificationRepository;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface BinderModule {
    @Binds
    NotificationProvider bindDefaultNotoficationProviderToNotificationProvider(DefaultNotificationProvider defaultNotificationProvider);

    @Binds
    NoteRepository bindDefaultNoteRepositoryToNoteRepository(DefaultNoteRepository defaultNoteRepository);

    @Binds
    NotificationRepository bindDefaultNotificationRepositoryToNotificationRepository(DefaultNotificationRepository defaultNotificationRepository);
}
