package com.example.noteapplication.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
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

import java.util.Date;

public class NoteNotificationDialogFragment extends DialogFragment {
    private static final String TAG = "NotificationDialogFragment";

    private FragmentNotificationDialogBinding _binding;

    public FragmentNotificationDialogBinding getBinding() {
        return _binding;
    }

    private boolean isPickedTodayDate = false, isPickedCurrentHour = false;
    private String[] dates;
    private String pickedDate;
    private int pickedDateIndex = 0, cachedDateIndex = 0;
    private String pickedHour;
    private int pickedHourIndex = 0, cachedHourIndex = 0;
    private String pickedMinute;
    private int pickedMinuteIndex = 0, cachedMinuteIndex = 0;
    private String pickedFullDate;

    public interface OnSubmitNotificationDateListener {
        void onSubmitDatePick(Date date, Boolean isNotificationSet, NoteDateSelectionIndexSaver dateSelection);
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

        setLocalDate();
        setPickerFormats();
        setDatePicker();
        setHourPicker();
        setMinutePicker();
        prepareNotificationDateSelectionIndices();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.notification_title)
                .setView(_binding.getRoot())
                .setNegativeButton(getString(R.string.notification_cancel), null)
                .setPositiveButton(getString(R.string.notification_set), null);

        return builder.create();
    }

    private void setLocalDate() {
        pickedDate = NoteUtils.DateManipulator.getCurrentDateString();
        pickedHour = NoteUtils.DateManipulator.getCurrentHourString();
        pickedMinute = NoteUtils.DateManipulator.getCurrentMinuteString();
        pickedFullDate = NoteUtils.DateManipulator.getCurrentFullDateString();

        cachedDateIndex = 0;
        cachedHourIndex = NoteUtils.DateManipulator.getCurrentHour();
        cachedMinuteIndex = NoteUtils.DateManipulator.getCurrentMinute();

        dates = NoteUtils.DateManipulator.getDatePickers();

        long plannedNotificationDateInMillis = requireArguments().getLong(
                NoteTransactionDataKeys.NOTIFICATION_SET_DATA_KEY, 0L
        );
        if (plannedNotificationDateInMillis != 0) {
            Date currentNotificationDate = NoteUtils.DateManipulator.parseStringToFullDate(pickedFullDate);

            long currentNotificationDateInMillis = NoteUtils.DateManipulator.getDateTimeInMillis(currentNotificationDate);
            long dateDiff = plannedNotificationDateInMillis - currentNotificationDateInMillis;

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

    private void setPickerFormats() {
        _binding.notificationHourPicker.setFormatter(new NumberPicker.Formatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        _binding.notificationMinutePicker.setFormatter(new NumberPicker.Formatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
    }

    private void setDatePicker() {
        _binding.notificationDatePicker.setMinValue(0);
        _binding.notificationDatePicker.setMaxValue(dates.length - 1);
        _binding.notificationDatePicker.setDisplayedValues(dates);

        _binding.notificationDatePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String[] displayedDateValues = picker.getDisplayedValues();
                pickedDate = displayedDateValues[newVal];
                cachedDateIndex = newVal;

                isPickedTodayDate = NoteUtils.DateManipulator.isPickedTodayDate(cachedDateIndex);

                notifyLocalDateChanged();
                setHourPicker();
                setMinutePicker();
            }
        });

        isPickedTodayDate = NoteUtils.DateManipulator.isPickedTodayDate(cachedDateIndex);
    }

    private void setHourPicker() {
        if (isPickedTodayDate) _binding.notificationHourPicker.setMinValue(NoteUtils.DateManipulator.getMinHourIndex());
        else _binding.notificationHourPicker.setMinValue(0);
        _binding.notificationHourPicker.setMaxValue(23);

        _binding.notificationHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pickedHour = Integer.toString(newVal);
                pickedHourIndex = newVal;

                isPickedCurrentHour = NoteUtils.DateManipulator.isPickedCurrentHour(pickedHourIndex);

                notifyLocalDateChanged();
                setMinutePicker();
            }
        });

        pickedHourIndex = _binding.notificationHourPicker.getValue();
        isPickedCurrentHour = NoteUtils.DateManipulator.isPickedCurrentHour(pickedHourIndex);
    }

    private void setMinutePicker() {
        if (isPickedTodayDate && isPickedCurrentHour)
            _binding.notificationMinutePicker.setMinValue(NoteUtils.DateManipulator.getMinMinuteIndex());
        else _binding.notificationMinutePicker.setMinValue(0);
        _binding.notificationMinutePicker.setMaxValue(59);

        _binding.notificationMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pickedMinute = Integer.toString(newVal);
                pickedMinuteIndex = newVal;

                notifyLocalDateChanged();
            }
        });

        pickedMinuteIndex = _binding.notificationMinutePicker.getValue();
    }

    private void prepareNotificationDateSelectionIndices() {
        if (cachedDateIndex < dates.length) {
            _binding.notificationDatePicker.setValue(cachedDateIndex);
            pickedDate = _binding.notificationDatePicker.getDisplayedValues()[cachedDateIndex];
        }

        _binding.notificationHourPicker.setValue(cachedHourIndex);
        pickedHour = Integer.toString(cachedHourIndex);

        _binding.notificationMinutePicker.setValue(cachedMinuteIndex);
        pickedMinute = Integer.toString(cachedMinuteIndex);
    }

    private void notifyLocalDateChanged() {
        pickedFullDate = NoteUtils.DateManipulator.formatFullDate(pickedDate, pickedHour, pickedMinute);
        cachedHourIndex = pickedHourIndex;
        cachedMinuteIndex = pickedMinuteIndex;
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
                    notifyLocalDateChanged();

                    pickedDateIndex = cachedDateIndex;
                    pickedHourIndex = cachedHourIndex;
                    pickedMinuteIndex = cachedMinuteIndex;

                    notificationDateListener.onSubmitDatePick(
                            NoteUtils.DateManipulator.parseStringToFullDate(pickedFullDate),
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
