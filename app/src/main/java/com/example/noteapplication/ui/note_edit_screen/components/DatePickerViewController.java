package com.example.noteapplication.ui.note_edit_screen.components;

import android.content.Context;
import android.widget.NumberPicker;

public class DatePickerViewController {

    private final DatePickerManager datePickerManager;
    private final NumberPicker dayPicker;
    private final NumberPicker hourPicker;
    private final NumberPicker minutePicker;

    public DatePickerViewController(Context context, NumberPicker dayPicker, NumberPicker hourPicker, NumberPicker minutePicker) {
        this.datePickerManager = new DatePickerManager(context);
        this.dayPicker = dayPicker;
        this.hourPicker = hourPicker;
        this.minutePicker = minutePicker;

        setUp();
    }

    private void setUp() {
        setUpDayPicker();
        setUpHourPicker();
        setUpMinutePicker();
    }

    private void setUpDayPicker() {
        dayPicker.setDisplayedValues(datePickerManager.getDayPicker());
        dayPicker.setMinValue(0);
        dayPicker.setMaxValue(datePickerManager.getDayPicker().length - 1);
    }

    private void setUpHourPicker() {
        hourPicker.setDisplayedValues(datePickerManager.getHourPicker());
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(datePickerManager.getHourPicker().length - 1);
    }

    private void setUpMinutePicker() {
        minutePicker.setDisplayedValues(datePickerManager.getMinutePicker());
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(datePickerManager.getMinutePicker().length - 1);
    }

    public void selectTimeInMillis(long millis) {
        long timeInMillis = millis == 0L ? System.currentTimeMillis() : millis;
        dayPicker.setValue(datePickerManager.getDayByTimeInMillis(timeInMillis));
        hourPicker.setValue(datePickerManager.getHourByTimeInMillis(timeInMillis));
        minutePicker.setValue(datePickerManager.getMinuteByTimeInMillis(timeInMillis));
    }

    public Long getPickedTimeInMillis() {
        return datePickerManager.getDateByPickerValues(dayPicker.getValue(), hourPicker.getValue(), minutePicker.getValue());
    }

    public void setEnabled(boolean value) {
        dayPicker.setEnabled(value);
        hourPicker.setEnabled(value);
        minutePicker.setEnabled(value);
    }
}
