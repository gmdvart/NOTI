package com.example.noteapplication.data.repository;

import androidx.lifecycle.LiveData;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.database.NoteDao;
import com.example.noteapplication.data.database.NoteDatabase;
import com.example.noteapplication.domain.repository.NoteRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DefaultNoteRepository implements NoteRepository {

    private final NoteDao noteDao;
    private final ExecutorService databaseExecutor;

    @Inject
    public DefaultNoteRepository(NoteDatabase noteDatabase) {
        this.noteDao = noteDatabase.getDao();
        this.databaseExecutor = NoteDatabase.getDatabaseExecutor();
    }

    @Override
    public void insert(Note note) {
        databaseExecutor.execute(() -> noteDao.insert(note));
    }

    @Override
    public int insertAndGetRowId(Note note) {
        try {
            Future<Long> noteFuture = databaseExecutor.submit(() -> noteDao.insertAndGetRowId(note));
            return noteFuture.get().intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Note getById(int id) {
        try {
            Future<Note> noteFuture = databaseExecutor.submit(() -> noteDao.readById(id));
            return noteFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Note getByIdBlocking(int id) {
        return noteDao.readById(id);
    }

    public void insertBlocking(Note note) {
        noteDao.insert(note);
    }

    @Override
    public LiveData<List<Note>> getAllByFilterKey(int filterKey) {
        return noteDao.readByFilterKeyLiveDate(filterKey);
    }

    @Override
    public LiveData<List<Note>> getAllByQueryLiveData(String searchString) {
        return noteDao.readBySearchQueryLiveData("%" + searchString + "%");
    }

    @Override
    public void delete(Note note) {
        databaseExecutor.execute(() -> noteDao.delete(note));
    }
}
