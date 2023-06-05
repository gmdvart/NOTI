package com.example.noteapplication.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import com.example.noteapplication.R;
import com.example.noteapplication.constants.NoteFilterKeys;
import com.example.noteapplication.constants.NoteNotificationsKeys;
import com.example.noteapplication.constants.NoteTransactionDataKeys;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.databinding.FragmentNoteEditBinding;
import com.example.noteapplication.ui.NoteDateSelectionIndexSaver;
import com.example.noteapplication.ui.NoteViewModel;
import com.example.noteapplication.ui.adapter.NoteImportanceSelectionAdapter;
import com.example.noteapplication.utils.NoteUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NoteEditFragment extends Fragment implements MenuProvider, NoteNotificationDialogFragment.OnSubmitNotificationDateListener {
    private static final String TAG = "NoteEditFragment";

    private FragmentNoteEditBinding _binding;
    public FragmentNoteEditBinding getBinding() {
        return _binding;
    }

    private NavController navController;
    private NoteEditFragmentArgs navArgs;

    private NoteViewModel viewModel;

    // Note edit options
    private Note selectedNote = null;
    private String[] importanceSelectionList;
    private int selectedImportanceLevel = 0;
    private Long notificationDateInMillis = 0L;
    private String notificationDateString;
    private NoteDateSelectionIndexSaver dateSelectionIndices = new NoteDateSelectionIndexSaver(0, 0, 0);

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        importanceSelectionList = new String[] {
            getString(R.string.importance_none),
            getString(R.string.importance_low),
            getString(R.string.importance_medium),
            getString(R.string.importance_high)
        };

        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(NoteViewModel.initializer)).get(NoteViewModel.class);

        notificationDateString = getString(R.string.notification_never); // Default notification value which is used to calculate a full date (with year)

        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MenuHost) requireActivity()).addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(_binding.editToolbar);

        navArgs = NoteEditFragmentArgs.fromBundle(requireArguments());

        _binding.editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackArrowClick();
            }
        });

        setupImportanceOptions();
        setupDateField();

        // If we're already have a note
        if (navArgs.getNoteId() != -1) {
            viewModel.readNoteById(navArgs.getNoteId()).observe(getViewLifecycleOwner(), new Observer<Note>() {
                @Override
                public void onChanged(Note note) {
                    selectedNote = note;
                    setupNoteFields(selectedNote);
                }
            });
        }
    }

    private void onBackArrowClick() {
        NavDirections action = NoteEditFragmentDirections.actionNoteEditFragmentToNoteListFragment();
        navController.navigate(action);
    }

    private void setupImportanceOptions() {
        AutoCompleteTextView importanceSelection = _binding.noteEditImportance;
        NoteImportanceSelectionAdapter adapter = new NoteImportanceSelectionAdapter(requireContext(), importanceSelectionList);
        importanceSelection.setAdapter(adapter);
        importanceSelection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String importance = parent.getItemAtPosition(position).toString();
                setImportance(importance, position);
            }
        });
    }

    private void setImportance(String importance, int level) {
        selectedImportanceLevel = level;
        _binding.noteMenuItemIndicator
                .setImageResource(NoteUtils.ImportanceSelection.getImageResourceForImportanceByString(requireContext(), importance));
    }

    private void setupDateField() {
        _binding.noteEditNotificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle transactionData = getTransactionData();
                NoteNotificationDialogFragment dialog = new NoteNotificationDialogFragment();
                dialog.setArguments(transactionData);
                dialog.show(requireActivity().getSupportFragmentManager(), "NotificationDialogFragment");
            }
        });
    }

    private Bundle getTransactionData() {
        Bundle transactionData = new Bundle();
        transactionData.putLong(NoteTransactionDataKeys.NOTIFICATION_SET_DATA_KEY, notificationDateInMillis);
        if (notificationDateInMillis != 0) {
            transactionData.putInt(NoteTransactionDataKeys.NOTIFICATION_DATE_SELECTION_KEY, dateSelectionIndices.getDateSelectionIndex());
            transactionData.putInt(NoteTransactionDataKeys.NOTIFICATION_HOUR_SELECTION_KEY, dateSelectionIndices.getHourSelectionIndex());
            transactionData.putInt(NoteTransactionDataKeys.NOTIFICATION_MINUTE_SELECTION_KEY, dateSelectionIndices.getMinuteSelectionIndex());
            transactionData.putString(NoteTransactionDataKeys.NOTIFICATION_SET_DATE_STRING, notificationDateString);
        }
        return transactionData;
    }

    private void setupNoteFields(Note note) {
        int noteImportanceLevel = NoteUtils.ImportanceSelection.getImportanceLevel(note.importanceLevel);
        int importanceStringRes = NoteUtils.ImportanceSelection.getStringResourceByImportanceLevel(noteImportanceLevel);

        _binding.noteEditImportance.setText(getString(importanceStringRes), false);
        setImportance(getString(importanceStringRes), noteImportanceLevel);

        int notificationDateInSecs = note.notificationDate;
        if (notificationDateInSecs != 0) {
            notificationDateInMillis = notificationDateInSecs * 1000L;
            String formattedFullDate = NoteUtils.DateManipulator.getFormattedFullDate(notificationDateInMillis);
            String formattedDisplayedDate = NoteUtils.DateManipulator.getFormattedDisplayedDate(requireContext(), notificationDateInMillis);

            notificationDateString = formattedFullDate;
            _binding.noteEditNotificationDate.setText(formattedDisplayedDate);

            dateSelectionIndices = new NoteDateSelectionIndexSaver(
                    note.indices.getDateSelectionIndex(),
                    note.indices.getHourSelectionIndex(),
                    note.indices.getMinuteSelectionIndex()
            );
        }

        _binding.noteEditTitle.setText(note.title);
        _binding.noteEditDescription.setText(note.description);
    }

    private void createOrUpdateNote(boolean isUpdate) {
        int importanceLevel = selectedImportanceLevel;
        int notificationDateInSecs = (int) (notificationDateInMillis / 1000);

        String title = Objects.requireNonNull(_binding.noteEditTitle.getText()).toString();
        String description = Objects.requireNonNull(_binding.noteEditDescription.getText()).toString();

        Note note;
        if (!isUpdate) note = new Note();
        else note = selectedNote ;

        if (!isUpdate) note.id = (int) Calendar.getInstance().getTimeInMillis();

        if (!title.isEmpty()) note.title = title;
        else note.title = "";

        if (!description.isEmpty()) note.description = description;
        else note.description = "";

        note.importanceLevel = NoteUtils.ImportanceSelection.getImportanceLevel(importanceLevel);

        if (!isUpdate) {
            Date todayDate = Calendar.getInstance().getTime();
            note.creationDate = (int) (NoteUtils.DateManipulator.getDateTimeInMillis(todayDate) / 1000);
        }

        if (notificationDateInSecs != NoteNotificationsKeys.WITHOUT_NOTIFICATION) {
            note.notificationDate = notificationDateInSecs;
            note.indices = dateSelectionIndices;

            viewModel.setNotificationOnNote(note);
        } else {
            note.notificationDate = NoteNotificationsKeys.WITHOUT_NOTIFICATION;
            note.indices = new NoteDateSelectionIndexSaver(0, 0, 0);

            if (isUpdate)
                viewModel.cancelNoteNotification(note.id);
        }

        if (!isUpdate) viewModel.createNote(note);
        else viewModel.updateNote(note);
    }

    @Override
    public void onCreateMenu(@NotNull Menu menu, @NotNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.edit_note_toolbar_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NotNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.edit_done_item) {
            createOrUpdateNote(navArgs.getNoteId() != -1);
            NavDirections directions = NoteEditFragmentDirections.actionNoteEditFragmentToNoteListFragment();
            navController.navigate(directions);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSubmitDatePick(Date date, Boolean isNotificationSet, NoteDateSelectionIndexSaver dateSelection) {
        if (!isNotificationSet) {
            _binding.noteEditNotificationDate.setText(getString(R.string.notification_never));

            notificationDateInMillis = 0L;
            this.dateSelectionIndices = new NoteDateSelectionIndexSaver(0, 0, 0);
        } else {
            long notifyDateInMillisToSet = NoteUtils.DateManipulator.getDateTimeInMillis(date);

            String displayedDateString = NoteUtils.DateManipulator.getFormattedDisplayedDate(requireContext(),notifyDateInMillisToSet);
            String fullDateString = NoteUtils.DateManipulator.getFormattedFullDate(notifyDateInMillisToSet);
            _binding.noteEditNotificationDate.setText(displayedDateString);

            notificationDateInMillis = notifyDateInMillisToSet;
            this.dateSelectionIndices = dateSelection;
            notificationDateString = fullDateString;
        }
    }
}