package com.example.noteapplication.data.repository;

import androidx.lifecycle.LiveData;
import com.example.noteapplication.data.database.Note;

import java.util.List;

public interface NoteRepository {
    void createNote(Note note);
    void updateNote(Note note);
    void deleteNote(Note note);
    LiveData<List<Note>> getAllNotes();
}
