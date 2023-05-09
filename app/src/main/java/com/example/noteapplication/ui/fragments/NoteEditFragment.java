package com.example.noteapplication.ui.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
//import com.example.noteapplication.NoteEditFragmentArgs;
//import com.example.noteapplication.NoteEditFragmentDirections;
import com.example.noteapplication.R;
import com.example.noteapplication.constants.NotiTransactionDataKeys;
import com.example.noteapplication.databinding.FragmentNoteEditBinding;
import com.example.noteapplication.ui.NoteDateSelectionIndexSaver;
import com.example.noteapplication.ui.adapter.NoteImportanceSelectionAdapter;
import com.example.noteapplication.utils.NoteUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class NoteEditFragment extends Fragment implements MenuProvider, NoteNotificationDialogFragment.OnSubmitNotificationDateListener {
    private static final String TAG = "NoteEditFragment";

    private FragmentNoteEditBinding _binding;
    public FragmentNoteEditBinding getBinding() {
        return _binding;
    }

    private NavController navController;
    private NoteEditFragmentArgs navArgs;

    // Note edit options
    private String[] importanceSelectionList;
    private Long notificationDateInMillis = 0L;
    private String notificationDateString;
    private NoteDateSelectionIndexSaver dateSelection = new NoteDateSelectionIndexSaver(0, 0, 0);

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

        notificationDateString = getString(R.string.notification_never);

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
                setImportanceIcon(importance);
            }
        });
    }

    private void setImportanceIcon(String importance) {
        _binding.noteMenuItemIndicator
                .setImageResource(NoteUtils.ImportanceSelection.getImageResourceForImportanceByString(importance));
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
        transactionData.putLong(NotiTransactionDataKeys.NOTIFICATION_SET_DATA_KEY, notificationDateInMillis);
        if (notificationDateInMillis != 0) {
            transactionData.putInt(NotiTransactionDataKeys.NOTIFICATION_DATE_SELECTION_KEY, dateSelection.getDateSelectionIndex());
            transactionData.putInt(NotiTransactionDataKeys.NOTIFICATION_HOUR_SELECTION_KEY, dateSelection.getHourSelectionIndex());
            transactionData.putInt(NotiTransactionDataKeys.NOTIFICATION_MINUTE_SELECTION_KEY, dateSelection.getMinuteSelectionIndex());
            transactionData.putString(NotiTransactionDataKeys.NOTIFICATION_SET_DATE_STRING, notificationDateString);
        }
        return transactionData;
    }

    @Override
    public void onCreateMenu(@NotNull Menu menu, @NotNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.edit_note_toolbar_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NotNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.edit_done_item) {
            Toast.makeText(requireActivity(), "Clicked on Done", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSubmitDatePick(String date, Boolean isNotificationSet, NoteDateSelectionIndexSaver dateSelection) {
        if (!isNotificationSet) {
            _binding.noteEditNotificationDate.setText(getString(R.string.notification_never));

            notificationDateInMillis = 0L;
            dateSelection = new NoteDateSelectionIndexSaver(0, 0, 0);
        } else {
            _binding.noteEditNotificationDate.setText(date);

            Date notificationDate = NoteUtils.DateManipulator.parseStringToFullDate(date);
            notificationDateInMillis = NoteUtils.DateManipulator.getDateTimeInMillis(notificationDate);
            this.dateSelection = dateSelection;
            notificationDateString = date;
        }
    }
}