package com.example.noteapplication.ui;

public class DateSelectionIndexSaver {
    private final int date;
    private final int hour;
    private final int minute;

    public DateSelectionIndexSaver(int date, int hour, int minute) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    public int getDateSelectionIndex() {
        return date;
    }

    public int getHourSelectionIndex() {
        return hour;
    }

    public int getMinuteSelectionIndex() {
        return minute;
    }
}
