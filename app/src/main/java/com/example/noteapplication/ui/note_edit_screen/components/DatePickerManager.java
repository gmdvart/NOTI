package com.example.noteapplication.ui.note_edit_screen.components;

import android.content.Context;
import android.util.Log;
import com.example.noteapplication.R;

import java.text.SimpleDateFormat;
import java.util.*;

public class DatePickerManager {
    private String[] dayPicker;
    private String[] hourPicker;
    private String[] minutePicker;

    private final Context context;

    public DatePickerManager(Context context) {
        this.context = context;
        prepareDayPicker();
        prepareHourPicker();
        prepareMinutePicker();
    }

    private void prepareDayPicker() {
        int lastDayOfYear = 31;
        int lastMonthOfYear = 11;
        Calendar greaterBoundCalendar = Calendar.getInstance();
        Calendar lowerBoundCalendar = Calendar.getInstance();

        greaterBoundCalendar.set(greaterBoundCalendar.get(Calendar.YEAR), lastMonthOfYear, lastDayOfYear);
        int remainingDaysInYear = greaterBoundCalendar.get(Calendar.DAY_OF_YEAR) - lowerBoundCalendar.get(Calendar.DAY_OF_YEAR);

        dayPicker = new String[remainingDaysInYear + 1];
        for (int i = 0; i <= remainingDaysInYear; i++) {
            dayPicker[i] = getTimeStringByFormatPattern(context.getString(R.string.day_format), lowerBoundCalendar.getTimeInMillis());
            lowerBoundCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void prepareHourPicker() {
        int firstHourOfDay = 0;
        int lastHourOfDay = 23;
        Calendar lowerBoundCalendar = Calendar.getInstance();
        Calendar greaterBoundsCalendar = Calendar.getInstance();

        lowerBoundCalendar.set(Calendar.HOUR_OF_DAY, firstHourOfDay);
        greaterBoundsCalendar.set(Calendar.HOUR_OF_DAY, lastHourOfDay);
        int hoursInDay = greaterBoundsCalendar.get(Calendar.HOUR_OF_DAY) - lowerBoundCalendar.get(Calendar.HOUR_OF_DAY);

        hourPicker = new String[hoursInDay + 1];
        for (int i = 0; i <= hoursInDay; i++) {
            hourPicker[i] = getTimeStringByFormatPattern(context.getString(R.string.hour_format), lowerBoundCalendar.getTimeInMillis());
            lowerBoundCalendar.add(Calendar.HOUR_OF_DAY, 1);
        }
    }

    private void prepareMinutePicker() {
        int firstMinuteOfHour = 0;
        int lastMinuteOfHour = 59;
        Calendar lowerBoundCalendar = Calendar.getInstance();
        Calendar greaterBoundCalendar = Calendar.getInstance();

        lowerBoundCalendar.set(Calendar.MINUTE, firstMinuteOfHour);
        greaterBoundCalendar.set(Calendar.MINUTE, lastMinuteOfHour);
        int minutesInHour = greaterBoundCalendar.get(Calendar.MINUTE) - lowerBoundCalendar.get(Calendar.MINUTE);

        minutePicker = new String[minutesInHour + 1];
        for (int i = 0; i <= minutesInHour; i++) {
            minutePicker[i] = getTimeStringByFormatPattern(context.getString(R.string.minute_format), lowerBoundCalendar.getTimeInMillis());
            lowerBoundCalendar.add(Calendar.MINUTE, 1);
        }
    }

    public Long getDateByPickerValues(int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, day); // Here we're using add instead of set because we need to take into account current day
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }

    public int getDayByTimeInMillis(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return calendar.get(Calendar.DAY_OF_YEAR) - Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    public int getHourByTimeInMillis(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinuteByTimeInMillis(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        return calendar.get(Calendar.MINUTE);
    }

    public String[] getDayPicker() {
        return dayPicker;
    }

    public String[] getHourPicker() {
        return hourPicker;
    }

    public String[] getMinutePicker() {
        return minutePicker;
    }

    private String getTimeStringByFormatPattern(String formatPattern, long timeInMillis) {
        SimpleDateFormat dateFormat= new SimpleDateFormat(formatPattern, Locale.getDefault());
        Date date = new Date(timeInMillis);
        return dateFormat.format(date);
    }
}
