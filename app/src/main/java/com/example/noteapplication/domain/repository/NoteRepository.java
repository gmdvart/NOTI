package com.example.noteapplication.domain.repository;

import androidx.lifecycle.LiveData;
import com.example.noteapplication.data.database.Note;

import java.util.List;

public interface NoteRepository {
    void insert(Note note);
    int insertAndGetRowId(Note note);
    Note getById(int id);
    LiveData<List<Note>> getAllByFilterKey(int filterKey);
    LiveData<List<Note>> getAllByQueryLiveData(String searchString);
    void delete(Note note);
}
