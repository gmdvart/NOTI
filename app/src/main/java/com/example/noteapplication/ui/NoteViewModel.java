package com.example.noteapplication.ui;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import com.example.noteapplication.NoteApplication;
import com.example.noteapplication.constants.NoteFilterKeys;
import com.example.noteapplication.data.app.AppDataContainer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.repository.NoteNotificationRepository;
import com.example.noteapplication.data.repository.NoteRepository;
import kotlin.jvm.functions.Function1;

import java.util.List;

public class NoteViewModel extends ViewModel {
    private final NoteRepository noteRepository;
    private final NoteNotificationRepository notificationRepository;

    public NoteViewModel(NoteRepository noteRepository, NoteNotificationRepository notificationRepository) {
        this.noteRepository = noteRepository;
        this.notificationRepository = notificationRepository;
    }

    public void createNote(Note note) {
        noteRepository.createNote(note);
    }
    public void updateNote(Note note) {
        noteRepository.updateNote(note);
    }
    public void deleteNote(Note note) {
        noteRepository.deleteNote(note);
    }
    public LiveData<Note> readNoteById(int id) {
        return noteRepository.readNoteById(id);
    }
    public LiveData<List<Note>> readFilteredNotes(int filterKey) {
        return noteRepository.readFilteredNotes(filterKey);
    }
    public LiveData<List<Note>> searchForNotes(String searchString) {
        return noteRepository.searchNotes(searchString);
    }

    public void setNotificationOnNote(Note note) {
        notificationRepository.makeNotification(note);
    }

    public static final ViewModelInitializer<NoteViewModel> initializer = new ViewModelInitializer<>(
            NoteViewModel.class, new Function1<CreationExtras, NoteViewModel>() {
        @Override
        public NoteViewModel invoke(CreationExtras creationExtras) {
            NoteApplication application = (NoteApplication) creationExtras.get(APPLICATION_KEY);
            assert application != null;
            AppDataContainer container = application.getContainer();

            NoteRepository noteRepository = container.getNoteRepository();
            NoteNotificationRepository notificationRepository = container.getNotificationRepository();

            return new NoteViewModel(noteRepository, notificationRepository);
        }
    });
}
