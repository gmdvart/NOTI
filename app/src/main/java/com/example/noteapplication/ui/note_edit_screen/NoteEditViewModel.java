package com.example.noteapplication.ui.note_edit_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import com.example.noteapplication.NoteApplication;
import com.example.noteapplication.di.ApplicationComponent;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.domain.repository.NoteRepository;
import com.example.noteapplication.domain.repository.NotificationRepository;

public class NoteEditViewModel extends ViewModel {

    private final NoteRepository noteRepository;
    private final NotificationRepository notificationRepository;

    public NoteEditViewModel(NoteRepository noteRepository, NotificationRepository notificationRepository) {
        this.noteRepository = noteRepository;
        this.notificationRepository = notificationRepository;
    }

    private Note loadedNote = null;

    // UI State fields
    private final MutableLiveData<Integer> _importanceLevel = new MutableLiveData<>(0);
    public LiveData<Integer> importanceLevel = _importanceLevel;

    private final MutableLiveData<Long> _notificationDateInMillis = new MutableLiveData<>(0L);
    public LiveData<Long> notificationDateInMillis = _notificationDateInMillis;

    private final MutableLiveData<Boolean> _notificationDateSetError = new MutableLiveData<>(false);
    public LiveData<Boolean> notificationDateSetError = _notificationDateSetError;

    private boolean isNotificationSwitcherChecked = true;

    private String title = "";
    public String getTitle() {
        return title;
    }

    private String description = "";
    public String getDescription() {
        return description;
    }

    public void loadNodeById(int noteId) {
        if (noteId == -1) return;

        loadedNote = noteRepository.getById(noteId);
        _importanceLevel.setValue(loadedNote.importanceLevel);
        _notificationDateInMillis.setValue(loadedNote.notificationDate);
        title = loadedNote.title;
        description = loadedNote.description;
    }

    public void onEvent(Event event) {
        if (event instanceof Event.ImportanceLevelChanged) {
            int importanceLevel = ((Event.ImportanceLevelChanged) event).value;
            _importanceLevel.setValue(importanceLevel);
        }
        if (event instanceof Event.NotificationDateChanged) {
            Long notificationDateInMillis = ((Event.NotificationDateChanged) event).value;
            tryToSetNotificationDate(notificationDateInMillis);
        }
        if (event instanceof Event.TitleChanged) {
            title = ((Event.TitleChanged) event).value;
        }
        if (event instanceof Event.DescriptionChanged) {
            description = ((Event.DescriptionChanged) event).value;
        }
        if (event instanceof Event.NotificationSwitcherChanged) {
            isNotificationSwitcherChecked = ((Event.NotificationSwitcherChanged) event).value;
        }
        if (event instanceof Event.WriteNote) {
            writeNote();
        }
        if (event instanceof Event.NavigateBack) {
            resetAllFields();
        }
    }

    private void tryToSetNotificationDate(Long notificationDateInMillis) {
        /// We set notificationDateInMillis equal to zero in cases where we won't actually set a notification,
        /// basically zero means that notification date was not set.
        /// Thus, we need to include this fact when checking condition.
        long timeInMillis = (notificationDateInMillis <= System.currentTimeMillis()) ? 0L : notificationDateInMillis;
        _notificationDateInMillis.setValue(timeInMillis);
        _notificationDateSetError.setValue(timeInMillis == 0L && isNotificationSwitcherChecked);
    }

    private void resetAllFields() {
        _importanceLevel.setValue(0);
        _notificationDateInMillis.setValue(0L);
        _notificationDateSetError.setValue(false);
        title = "";
        description = "";
    }

    private void writeNote() {
        // Creating note in repository
        Note noteToBeWrote = loadedNote == null ? new Note() : loadedNote;
        noteToBeWrote.importanceLevel = _importanceLevel.getValue();
        noteToBeWrote.notificationDate = _notificationDateInMillis.getValue();
        noteToBeWrote.title = title;
        noteToBeWrote.description = description;
        noteToBeWrote.creationDate = System.currentTimeMillis();
        int noteId = noteRepository.insertAndGetRowId(noteToBeWrote);
        // Depending on notification date was set a notification will be either scheduled or removed
        // We're setting the just added note's id so the alarm manager can put in into intent
        noteToBeWrote.id = noteId;
        if (isNotificationSwitcherChecked) notificationRepository.create(noteToBeWrote);
        else notificationRepository.remove(noteToBeWrote);
    }

    public interface Event {
        final class ImportanceLevelChanged implements Event {
            public final int value;

            public ImportanceLevelChanged(int value) {
                this.value = value;
            }
        }

        final class NotificationDateChanged implements Event {
            public final long value;

            public NotificationDateChanged(long value) {
                this.value = value;
            }
        }

        final class NotificationSwitcherChanged implements Event {
            public final boolean value;

            public NotificationSwitcherChanged(boolean value) {
                this.value = value;
            }
        }

        final class TitleChanged implements Event {
            public final String value;

            public TitleChanged(String value) {
                this.value = value;
            }
        }

        final class DescriptionChanged implements Event {
            public final String value;

            public DescriptionChanged(String value) {
                this.value = value;
            }
        }

        final class WriteNote implements Event { }

        final class NavigateBack implements Event {  }
    }

    public static ViewModelInitializer<NoteEditViewModel> initializer = new ViewModelInitializer<>(NoteEditViewModel.class, creationExtras -> {
        NoteApplication application = ((NoteApplication) creationExtras.get(APPLICATION_KEY));
        assert application != null;

        ApplicationComponent container = application.getAppComponent();
        NoteRepository noteRepository = container.getNoteRepository();
        NotificationRepository notificationRepository = container.getNotificationRepository();

        return new NoteEditViewModel(noteRepository, notificationRepository);
    });
}
