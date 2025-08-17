package com.example.noteapplication.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface NoteDao {
    @Upsert
    void insert(Note note);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAndGetRowId(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM note WHERE id = :id")
    Note readById(int id);

    @Query(
            "SELECT * FROM note ORDER BY" +
                    " CASE WHEN :filterKey = 0 THEN id END ASC," +
                    " CASE WHEN :filterKey = 1 THEN importance_level END DESC," +
                    " CASE WHEN :filterKey = 2 THEN notification_date END DESC," +
                    " CASE WHEN :filterKey = 3 THEN creation_date END ASC," +
                    " CASE WHEN :filterKey = 4 THEN title END ASC" + ";"
    )
    LiveData<List<Note>> readByFilterKeyLiveDate(int filterKey);

    @Query("SELECT * FROM note WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    LiveData<List<Note>> readBySearchQueryLiveData(String searchQuery);
}
