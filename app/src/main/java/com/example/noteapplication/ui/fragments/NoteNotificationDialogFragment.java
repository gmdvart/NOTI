package com.example.noteapplication.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.noteapplication.R;
import com.example.noteapplication.constants.NoteTransactionDataKeys;
import com.example.noteapplication.databinding.FragmentNotificationDialogBinding;
import com.example.noteapplication.ui.NoteDateSelectionIndexSaver;
import com.example.noteapplication.utils.NoteUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;

public class NoteNotificationDialogFragment extends DialogFragment {
    private static final String TAG = "NotificationDialogFragment";

    private FragmentNotificationDialogBinding _binding;
    public FragmentNotificationDialogBinding getBinding() {
        return _binding;
    }

    private String[] dates;
    private String[] hours;
    private int hoursCachedLength = 0;
    private String[] minutes;
    private int minuteCachedLength = 0;
    private String pickedDate;
    private int pickedDateIndex = 0, cachedDateIndex = 0;
    private String pickedHour;
    private int pickedHourIndex = 0, cachedHourIndex = 0;
    private String pickedMinute;
    private int pickedMinuteIndex = 0, cachedMinuteIndex = 0;
    private String pickedFullDate;

    public interface OnSubmitNotificationDateListener {
        void onSubmitDatePick(String date, Boolean isNotificationSet, NoteDateSelectionIndexSaver dateSelection);
    }
    private OnSubmitNotificationDateListener notificationDateListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        _binding = FragmentNotificationDialogBinding.inflate(inflater, null, false);

        _binding.notificationDatePicker.setWrapSelectorWheel(false);
        _binding.notificationHourPicker.setWrapSelectorWheel(false);
        _binding.notificationMinutePicker.setWrapSelectorWheel(false);

        setupLocalDate();
        setupDatePicker();
        setupHourPicker();
        setupMinutePicker();
        prepareDateSelectionIndices();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.notification_title)
                .setView(_binding.getRoot())
                .setNegativeButton(getString(R.string.notification_cancel), null)
                .setPositiveButton(getString(R.string.notification_set), null);

        return builder.create();
    }

    private void setupLocalDate() {
        pickedDate = NoteUtils.DateManipulator.getCurrentDate();
        pickedHour = NoteUtils.DateManipulator.getCurrentHour();
        pickedMinute = NoteUtils.DateManipulator.getCurrentMinute();
        pickedFullDate = NoteUtils.DateManipulator.getCurrentFullDate();

        long plannedNotificationDate = requireArguments().getLong(
                NoteTransactionDataKeys.NOTIFICATION_SET_DATA_KEY, 0L
        );
        if (plannedNotificationDate != 0) {
            Date currentNotificationDate = NoteUtils.DateManipulator.parseStringToFullDate(pickedFullDate);
            long currentNotificationDateInMillis = NoteUtils.DateManipulator.getDateTimeInMillis(currentNotificationDate);
            long dateDiff = plannedNotificationDate - currentNotificationDateInMillis;

            if (dateDiff > 0) {
                cachedDateIndex = requireArguments().getInt(NoteTransactionDataKeys.NOTIFICATION_DATE_SELECTION_KEY);
                cachedHourIndex = requireArguments().getInt(NoteTransactionDataKeys.NOTIFICATION_HOUR_SELECTION_KEY);
                cachedMinuteIndex = requireArguments().getInt(NoteTransactionDataKeys.NOTIFICATION_MINUTE_SELECTION_KEY);
                pickedFullDate = requireArguments().getString(NoteTransactionDataKeys.NOTIFICATION_SET_DATE_STRING);
            }
        } else {
            _binding.notificationSwitcher.setVisibility(View.GONE);
        }
    }

    private void setupDatePicker() {
        dates = NoteUtils.DateManipulator.getDatePickers();

        _binding.notificationDatePicker.setMinValue(0);
        _binding.notificationDatePicker.setMaxValue(dates.length - 1);
        _binding.notificationDatePicker.setDisplayedValues(dates);

        _binding.notificationDatePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String[] displayedDateValues = picker.getDisplayedValues();
                pickedDate = displayedDateValues[newVal];
                cachedDateIndex = newVal;

                notifyLocalDateChanged();
                setupHourPicker();
                setupMinutePicker();
            }
        });
    }

    private void setupHourPicker() {
        hours = NoteUtils.DateManipulator.getHourPicker(pickedFullDate);

        if (hoursCachedLength != hours.length) hoursCachedLength = hours.length;
        else return;

        _binding.notificationHourPicker.setValue(0);
        if (hours.length != 1) {
            _binding.notificationHourPicker.setMaxValue(hours.length - 1);
            _binding.notificationHourPicker.setMinValue(0);
        }
        _binding.notificationHourPicker.setDisplayedValues(hours);

        _binding.notificationHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String[] displayedHourValues = picker.getDisplayedValues();
                pickedHour = displayedHourValues[newVal];
                cachedHourIndex = newVal;

                notifyLocalDateChanged();
                setupMinutePicker();
            }
        });

        pickedHourIndex = _binding.notificationHourPicker.getValue();
        pickedHour = hours[pickedHourIndex];
    }

    private void setupMinutePicker() {
        minutes = NoteUtils.DateManipulator.getMinutePicker(pickedFullDate);

        if (minuteCachedLength != minutes.length) minuteCachedLength = minutes.length;
        else return;

        _binding.notificationMinutePicker.setValue(0);
        if (minutes.length != 1) {
            _binding.notificationMinutePicker.setMaxValue(minutes.length - 1);
            _binding.notificationMinutePicker.setMinValue(0);
        }
        _binding.notificationMinutePicker.setDisplayedValues(minutes);

        _binding.notificationMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String[] displayedMinuteValues = picker.getDisplayedValues();
                pickedMinute = displayedMinuteValues[newVal];
                cachedMinuteIndex = newVal;

                notifyLocalDateChanged();
            }
        });

        pickedMinuteIndex = _binding.notificationMinutePicker.getValue();
        pickedMinute = minutes[pickedMinuteIndex];
    }

    private void prepareDateSelectionIndices() {
        if (cachedDateIndex < dates.length) {
            _binding.notificationDatePicker.setValue(cachedDateIndex);
            pickedDate = _binding.notificationDatePicker.getDisplayedValues()[cachedDateIndex];
        }
        if (cachedHourIndex < hours.length) {
            _binding.notificationHourPicker.setValue(cachedHourIndex);
            pickedHour = _binding.notificationHourPicker.getDisplayedValues()[cachedHourIndex];
        }
        if (cachedMinuteIndex < minutes.length) {
            _binding.notificationMinutePicker.setValue(cachedMinuteIndex);
            pickedMinute = _binding.notificationMinutePicker.getDisplayedValues()[cachedMinuteIndex];
        }
    }

    private void notifyLocalDateChanged() {
        pickedFullDate = NoteUtils.DateManipulator.formatFullDate(pickedDate, pickedHour, pickedMinute);
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog dialog = (AlertDialog) requireDialog();
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationDateListener != null) {
                    pickedDateIndex = cachedDateIndex;
                    pickedHourIndex = cachedHourIndex;
                    pickedMinuteIndex = cachedMinuteIndex;
                    notifyLocalDateChanged();
                    notificationDateListener.onSubmitDatePick(
                            pickedFullDate,
                            _binding.notificationSwitcher.isChecked(),
                            new NoteDateSelectionIndexSaver(pickedDateIndex, pickedHourIndex, pickedMinuteIndex)
                    );
                }
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);

            if (navHostFragment != null) {
                notificationDateListener = (OnSubmitNotificationDateListener) navHostFragment
                        .getChildFragmentManager()
                        .getFragments()
                        .get(0);
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must to implement OnSubmitNotificationDateListener");
        }
    }
}
