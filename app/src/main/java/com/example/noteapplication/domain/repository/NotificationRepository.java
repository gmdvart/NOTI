package com.example.noteapplication.domain.repository;

import com.example.noteapplication.data.database.Note;

public interface NotificationRepository {
    void create(Note note);
    void remove(Note note);
}
