package com.example.noteapplication.ui.note_list_screen;

import androidx.lifecycle.*;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.domain.repository.NoteRepository;
import com.example.noteapplication.domain.repository.NotificationRepository;
import com.example.noteapplication.ui.constants.NoteFilterKeys;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class NoteListViewModel extends ViewModel {

    private final NoteRepository noteRepository;
    private final NotificationRepository notificationRepository;

    public NoteListViewModel(NoteRepository noteRepository, NotificationRepository notificationRepository) {
        this.noteRepository = noteRepository;
        this.notificationRepository = notificationRepository;

        noteList = Transformations.switchMap(searchRequest, request -> {
            if (request instanceof SearchRequest.ByFilter) {
                int filterKey = ((SearchRequest.ByFilter) request).value;
                return noteRepository.getAllByFilterKey(filterKey);
            }
            if (request instanceof SearchRequest.ByQuery) {
                String searchQuery = ((SearchRequest.ByQuery) request).value;
                return noteRepository.getAllByQueryLiveData(searchQuery);
            }
            return new MutableLiveData<>(Collections.emptyList());
        });
    }

    private final MutableLiveData<SearchRequest> searchRequest = new MutableLiveData<>(new SearchRequest.ByFilter(NoteFilterKeys.DEFAULT));

    public final LiveData<List<Note>> noteList;

    private final MutableLiveData<Note> longClickNote = new MutableLiveData<>();

    public void onEvent(Event event) {
        if (event instanceof Event.SearchQueryChanged) {
            String searchQuery = ((Event.SearchQueryChanged) event).value;
            searchRequest.setValue(new SearchRequest.ByQuery(searchQuery));
        }
        if (event instanceof Event.FilterKeyChanged) {
            int filterKey = ((Event.FilterKeyChanged) event).value;
            searchRequest.setValue(new SearchRequest.ByFilter(filterKey));
        }
        if (event instanceof Event.LongClickNoteChanged) {
            Note note = ((Event.LongClickNoteChanged) event).value;
            if (note != null) longClickNote.setValue(note);
        }
        if (event instanceof Event.DeleteNote) {
            Note note = longClickNote.getValue();
            if (note != null) {
                notificationRepository.remove(note);
                noteRepository.delete(note);
            }
        }
    }

    public interface Event {
        final class SearchQueryChanged implements Event {
            public final String value;
            public SearchQueryChanged(String value) {
                this.value = value;
            }
        }

        final class FilterKeyChanged implements Event {
            public final int value;
            public FilterKeyChanged(int value) {
                this.value = value;
            }
        }

        final class LongClickNoteChanged implements Event {
            public final Note value;
            public LongClickNoteChanged(Note value) {
                this.value = value;
            }
        }

        class DeleteNote implements Event { }
    }

    private interface SearchRequest {
        final class ByFilter implements SearchRequest {
            public final int value;
            public ByFilter(int value) {
                this.value = value;
            }
        }

        final class ByQuery implements SearchRequest {
            public final String value;
            public ByQuery(String value) {
                this.value = value;
            }
        }
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final NoteRepository noteRepository;
        private final NotificationRepository notificationRepository;

        @Inject
        public Factory(NoteRepository noteRepository, NotificationRepository notificationRepository) {
            this.noteRepository = noteRepository;
            this.notificationRepository = notificationRepository;
        }

        @NotNull
        @Override
        public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(NoteListViewModel.class)) return (T) new NoteListViewModel(noteRepository, notificationRepository);
            else throw new IllegalArgumentException("An unknown ViewModel class was provided");
        }
    }
}
