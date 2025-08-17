package com.example.noteapplication.ui.utils;

import android.content.Context;
import com.example.noteapplication.R;

import java.text.SimpleDateFormat;
import java.util.*;

public final class NoteUtils {
    public static class DateManipulator {
        public static final String TAG = "NotiUtils.DateManipulator";

        public static String getFormattedNotificationDataString(final Context context, long notificationDateInMillis) {
            if (notificationDateInMillis == 0L) return context.getString(R.string.notification_never);
            String pattern = context.getString(R.string.date_format);
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            return dateFormat.format(new Date(notificationDateInMillis));
        }

        public static String getForamttedCreationDateString(final Context context, long notificationDateInMillis) {
            String pattern = context.getString(R.string.date_format);
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            String formattedDate = dateFormat.format(new Date(notificationDateInMillis));
            return context.getString(R.string.created_at, formattedDate);
        }

        public static String getForamttedNotificationDateString(final Context context, long notificationDateInMillis) {
            String pattern = context.getString(R.string.date_format);
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            String formattedDate = dateFormat.format(new Date(notificationDateInMillis));
            return context.getString(R.string.notify_on, formattedDate);
        }
    }

    public static class ImportanceSelection {

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
    }
}
