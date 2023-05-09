package com.example.noteapplication.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import com.example.noteapplication.NoteApplication;
import com.example.noteapplication.data.app.AppDataContainer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.data.repository.NoteRepository;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NoteViewModel extends ViewModel {
    private final NoteRepository noteRepository;
    private final LiveData<List<Note>> allNotes;

    public NoteViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
        this.allNotes = noteRepository.getAllNotes();

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

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public static final ViewModelInitializer<NoteViewModel> initializer = new ViewModelInitializer<>(
            NoteViewModel.class, new Function1<CreationExtras, NoteViewModel>() {
        @Override
        public NoteViewModel invoke(CreationExtras creationExtras) {
            NoteApplication application = (NoteApplication) creationExtras.get(APPLICATION_KEY);
            assert application != null;
            AppDataContainer container = application.getContainer();
            NoteRepository repository = container.getNoteRepository();
            return new NoteViewModel(repository);
        }
    });
}
