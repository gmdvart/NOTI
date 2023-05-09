package com.example.noteapplication.ui;

public class NoteDateSelectionIndexSaver {
    private final int dateSelectionIndex;
    private final int hourSelectionIndex;
    private final int minuteSelectionIndex;

    public NoteDateSelectionIndexSaver(int dateSelectionIndex, int hourSelectionIndex, int minuteSelectionIndex) {
        this.dateSelectionIndex = dateSelectionIndex;
        this.hourSelectionIndex = hourSelectionIndex;
        this.minuteSelectionIndex = minuteSelectionIndex;
    }

    public int getDateSelectionIndex() {
        return dateSelectionIndex;
    }

    public int getHourSelectionIndex() {
        return hourSelectionIndex;
    }

    public int getMinuteSelectionIndex() {
        return minuteSelectionIndex;
    }
}
