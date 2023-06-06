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
        private static SimpleDateFormat hourFormatter = new SimpleDateFormat("HH", Locale.getDefault());
        private static SimpleDateFormat minuteFormatter = new SimpleDateFormat("mm", Locale.getDefault());
        private static SimpleDateFormat fullDateFormatter = new SimpleDateFormat("EEE, MMM d yyyy 'at' HH:mm", Locale.getDefault());

        private static SimpleDateFormat getDisplayedDateFormatter(final Context context) {
            String dateFormatPattern = context.getString(R.string.full_date_format);
            return new SimpleDateFormat(dateFormatPattern, Locale.getDefault());
        }
        private static SimpleDateFormat getDateFormatter(final Context context) {
            String dateFormatPattern = context.getString(R.string.date_format);
            return new SimpleDateFormat(dateFormatPattern, Locale.getDefault());
        }

        public static String getCurrentDateString(final Context context) {
            return getDateFormatter(context).format(Calendar.getInstance().getTime());
        }

        public static String getCurrentHourString() {
            return hourFormatter.format(Calendar.getInstance().getTime());
        }

        public static String getCurrentMinuteString() {
            return minuteFormatter.format(Calendar.getInstance().getTime());
        }

        public static String getCurrentFullDateString() {
            return fullDateFormatter.format(Calendar.getInstance().getTime());
        }

        public static String formatFullDate(String date, String hour, String minutes) {
            return date + " " + yearFormatter.format(Calendar.getInstance().getTime()) + " at " + hour + ":" + minutes;
        }
        public static String getFormattedFullDate(long date) {
            Date dateToFormat = new Date(date);
            return fullDateFormatter.format(dateToFormat);
        }
        public static String getFormattedDisplayedDate(Context context, long date) {
            Date dateToFormat = new Date(date);
            return getDisplayedDateFormatter(context).format(dateToFormat);
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

        public static String[] getDatePickers(final Context context) {
            Calendar dateCheckCalendar = Calendar.getInstance();
            Calendar currentCalendar = Calendar.getInstance();

            int finalDay = 31;
            int finalMonth = 11;
            int currentYear = currentCalendar.get(Calendar.YEAR);
            dateCheckCalendar.set(currentYear, finalMonth, finalDay);

            int daysInYear = dateCheckCalendar.get(Calendar.DAY_OF_YEAR) - currentCalendar.get(Calendar.DAY_OF_YEAR) + 1;
            String[] days = new String[daysInYear];

            for (int i = 0; i < daysInYear; i++) {
                days[i] = getDateFormatter(context).format(currentCalendar.getTime());
                currentCalendar.add(Calendar.DATE, 1);
            }
            return days;
        }

        public static boolean isPickedTodayDate(int pickedDateVal) {
            return Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + pickedDateVal == Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        }

        public static boolean isPickedCurrentHour(int pickedHourVal) {
            return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == pickedHourVal;
        }

        public static int getCurrentHour() {
            return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        }

        public static int getCurrentMinute() {
            return Calendar.getInstance().get(Calendar.MINUTE);
        }

        public static int getMinHourIndex() {
            return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        }

        public static int getMinMinuteIndex() {
            return Calendar.getInstance().get(Calendar.MINUTE);
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
