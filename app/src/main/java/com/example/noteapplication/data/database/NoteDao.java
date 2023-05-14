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
    LiveData<Note> readNoteById(int id);
    @Query(
            "SELECT * FROM notes ORDER BY" +
                    " CASE WHEN :filterKey = 0 THEN id END ASC," +
                    " CASE WHEN :filterKey = 1 THEN importance_level END ASC," +
                    " CASE WHEN :filterKey = 2 THEN notification_date END DESC," +
                    " CASE WHEN :filterKey = 3 THEN creation_date END ASC," +
                    " CASE WHEN :filterKey = 4 THEN title END ASC" + ";"
    )
    LiveData<List<Note>> readFilteredNotes(int filterKey);
    @Query("SELECT * FROM notes WHERE title LIKE :searchString OR description LIKE :searchString")
    LiveData<List<Note>> searchNotes(String searchString);
}
