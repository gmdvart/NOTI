package com.example.noteapplication.ui.note_edit_screen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.noteapplication.R;
import com.example.noteapplication.databinding.FragmentNotificationDialogBinding;
import com.example.noteapplication.ui.note_edit_screen.components.DatePickerViewController;

public class NoteNotificationDialogFragment extends DialogFragment {

    private FragmentNotificationDialogBinding _binding;

    private NoteEditViewModel viewModel;

    private DatePickerViewController datePickerViewController;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        _binding = FragmentNotificationDialogBinding.inflate(inflater, null, false);

        viewModel = new ViewModelProvider(requireActivity()).get(NoteEditViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.notification_title)
                .setView(_binding.getRoot())
                .setNegativeButton(getString(R.string.notification_cancel), null)
                .setPositiveButton(getString(R.string.notification_set), (dialog, which) -> { onSetButtonClick(); });

        return builder.create();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpDatePicker();
        setUpNotificationSwitcher();
    }

    private void setUpDatePicker() {
        datePickerViewController = new DatePickerViewController(
                requireContext(),
                _binding.notificationDayPicker,
                _binding.notificationHourPicker,
                _binding.notificationMinutePicker
        );

        viewModel.notificationDateInMillis.observe(getViewLifecycleOwner(), (millis) -> datePickerViewController.selectTimeInMillis(millis));
    }

    private void setUpNotificationSwitcher() {
        _binding.notificationSwitcher.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            viewModel.onEvent(new NoteEditViewModel.Event.NotificationSwitcherChanged(isChecked));
            datePickerViewController.setEnabled(isChecked);
        }));
    }

    private void onSetButtonClick() {
        if (_binding.notificationSwitcher.isChecked()) viewModel.onEvent(new NoteEditViewModel.Event.NotificationDateChanged(datePickerViewController.getPickedTimeInMillis()));
        else viewModel.onEvent(new NoteEditViewModel.Event.NotificationDateChanged(0L));
    }
}
