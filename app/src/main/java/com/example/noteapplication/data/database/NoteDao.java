package com.example.noteapplication.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNote(Note note);
    @Update
    void updateNote(Note note);
    @Delete
    void deleteNote(Note note);
    @Query("SELECT * FROM notes WHERE id = :id")
    LiveData<Note> getNoteById(int id);
    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getAllNotes();
}
