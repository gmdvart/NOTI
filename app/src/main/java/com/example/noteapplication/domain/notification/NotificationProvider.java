package com.example.noteapplication.domain.notification;

public interface NotificationProvider {

    /**
     * <p> Function is responsible for creating notification of scheduled note with given parameters. </p>
     * @param notificationId ID of the notification to be shown
     * @param title note's title
     * @param text note's text
     **/
    void create(int notificationId, String title, String text);
}
