package com.example.noteapplication.utils;

import android.content.Context;
import android.util.Log;
import com.example.noteapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class NoteUtils {
    public static class DateManipulator {
        private static final String TAG = "NotiUtils.DateManipulator";

        private static SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy", Locale.getDefault());
        private static SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        private static SimpleDateFormat hourFormatter = new SimpleDateFormat("HH", Locale.getDefault());
        private static SimpleDateFormat minuteFormatter = new SimpleDateFormat("mm", Locale.getDefault());
        private static SimpleDateFormat fullDateFormatter = new SimpleDateFormat("EEE, MMM d yyyy 'at' HH:mm", Locale.getDefault());

        public static String getCurrentDate() {
            return dateFormatter.format(Calendar.getInstance().getTime());
        }

        public static String getCurrentHour() {
            return hourFormatter.format(Calendar.getInstance().getTime());
        }

        public static String getCurrentMinute() {
            return minuteFormatter.format(Calendar.getInstance().getTime());
        }

        public static String getCurrentFullDate() {
            return fullDateFormatter.format(Calendar.getInstance().getTime());
        }

        public static String formatFullDate(String date, String hour, String minutes) {
            return date + " " + yearFormatter.format(Calendar.getInstance().getTime()) + " at " + hour + ":" + minutes;
        }

        public static String getFormattedFullDate(long date) {
            Date dateToFormat = new Date(date);
            return fullDateFormatter.format(dateToFormat);
        }

        public static Date parseStringToFullDate(String date) {
            try {
                return fullDateFormatter.parse(date);
            } catch (ParseException e) {
                Log.e(TAG, String.format("%1$S: %2$s", e.toString(), "Your string has incorrect date format!"));
                return Calendar.getInstance().getTime();
            }
        }
        public static Long getDateTimeInMillis(Date date) {
            Calendar time = Calendar.getInstance();
            time.setTime(date);
            return time.getTimeInMillis();
        }

        public static String[] getDatePickers() {
            Calendar dateCheckCalendar = Calendar.getInstance();
            Calendar currentCalendar = Calendar.getInstance();

            int finalDay = 31;
            int finalMonth = 11;
            int currentYear = currentCalendar.get(Calendar.YEAR);
            dateCheckCalendar.set(currentYear, finalMonth, finalDay);

            int daysInYear = dateCheckCalendar.get(Calendar.DAY_OF_YEAR) - currentCalendar.get(Calendar.DAY_OF_YEAR) + 1;
            String[] days = new String[daysInYear];

            for (int i = 0; i < daysInYear; i++) {
                days[i] = dateFormatter.format(currentCalendar.getTime());
                currentCalendar.add(Calendar.DATE, 1);
            }
            return days;
        }

        public static String[] getHourPicker(String fullDate) {
            int hoursInDay = 24;
            List<String> hours = new ArrayList<>();

            try {
                Date date = fullDateFormatter.parse(fullDate);
                if (date != null) {
                    Calendar setCalendar = Calendar.getInstance();
                    setCalendar.setTime(date);

                    Calendar currentCalendar = Calendar.getInstance();
                    int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
                    Calendar calendar = currentCalendar;
                    if (calendarMonthAndDayAreNotEqual(currentCalendar, setCalendar)) {
                        currentHour = 0;
                        calendar = setCalendar;
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                    }
                    for (int i = 0; i < hoursInDay - currentHour; i++) {
                        hours.add(hourFormatter.format(calendar.getTime()));
                        calendar.add(Calendar.HOUR_OF_DAY, 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return hours.toArray(new String[0]);
        }

        public static String[] getMinutePicker(String fullDate) {
            int minutesInHour = 60;
            List<String> minutes = new ArrayList<>();

            try {
                Date date = fullDateFormatter.parse(fullDate);
                if (date != null) {
                    Calendar setCalendar = Calendar.getInstance();
                    setCalendar.setTime(date);

                    Calendar currentCalendar = Calendar.getInstance();
                    int currentMinute = currentCalendar.get(Calendar.MINUTE);
                    Calendar calendar = currentCalendar;

                    if (calendarDatesAreNotEqual(currentCalendar, setCalendar)) {
                        currentMinute = 0;
                        calendar = setCalendar;
                        calendar.set(Calendar.MINUTE, 0);
                    }

                    for (int i = 0; i < minutesInHour - currentMinute; i++) {
                        minutes.add(minuteFormatter.format(calendar.getTime()));
                        calendar.add(Calendar.MINUTE, 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return minutes.toArray(new String[0]);
        }

        private static boolean calendarMonthAndDayAreNotEqual(Calendar currentCalendar, Calendar setCalendar) {
            return currentCalendar.get(Calendar.MONTH) != setCalendar.get(Calendar.MONTH) ||
                    currentCalendar.get(Calendar.DAY_OF_MONTH) != setCalendar.get(Calendar.DAY_OF_MONTH);
        }

        private static boolean calendarDatesAreNotEqual(Calendar currentCalendar, Calendar setCalendar) {
            return setCalendar.getTimeInMillis() > currentCalendar.getTimeInMillis();
        }
    }

    public static class ImportanceSelection {
        private static final int IMPORTANCE_SELECTION_COUNT = 4;

        public static int getImageResourceForImportanceByString(Context context, String importance) {
            String importanceHigh = context.getString(R.string.importance_high);
            String importanceMedium = context.getString(R.string.importance_medium);
            String importanceLow = context.getString(R.string.importance_low);

            if (Objects.equals(importance, importanceHigh)) return R.drawable.ic_importance_high;
            else if (Objects.equals(importance, importanceMedium)) return R.drawable.ic_importance_medium;
            else if (Objects.equals(importance, importanceLow)) return R.drawable.ic_importance_low;
            else return R.drawable.ic_importance_none;
        }

        public static int getImageResourceForImportanceByLevel(int level) {
            switch (level) {
                case 3:
                    return R.drawable.ic_importance_high;
                case 2:
                    return R.drawable.ic_importance_medium;
                case 1:
                    return R.drawable.ic_importance_low;
                default:
                    return R.drawable.ic_importance_none;
            }
        }

        public static int getStringResourceByImportanceLevel(int level) {
            switch (level) {
                case 3:
                    return R.string.importance_high;
                case 2:
                    return R.string.importance_medium;
                case 1:
                    return R.string.importance_low;
                default:
                    return R.string.importance_none;
            }
        }

        public static int getImportanceLevel(int noteImportanceLevel) {
            return IMPORTANCE_SELECTION_COUNT - (noteImportanceLevel + 1);
        }
    }
}
