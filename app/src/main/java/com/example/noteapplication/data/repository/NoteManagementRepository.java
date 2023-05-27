package com.example.noteapplication.data.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.database.NoteDao;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class NoteManagementRepository implements NoteRepository {
    private static final String TAG = "NoteManagementRepository";

    private final NoteDao noteDao;
    private final ExecutorService databaseExecutor;

    public NoteManagementRepository(NoteDao noteDao, ExecutorService databaseExecutor) {
        this.noteDao = noteDao;
        this.databaseExecutor = databaseExecutor;
    }

    @Override
    public void createNote(Note note) {
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.createNote(note);
            }
        });
    }

    @Override
    public void updateNote(Note note) {
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.updateNote(note);
            }
        });
    }

    @Override
    public void deleteNote(Note note) {
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                noteDao.deleteNote(note);
            }
        });
    }

    @Override
    public LiveData<Note> readNoteById(int id) {
        return noteDao.readNoteById(id);
    }

    @Override
    public LiveData<List<Note>> readFilteredNotes(int filterKey) {
        return noteDao.readFilteredNotes(filterKey);
    }

    @Override
    public LiveData<List<Note>> searchNotes(String searchString) {
        return noteDao.searchNotes(searchString);
    }
}
