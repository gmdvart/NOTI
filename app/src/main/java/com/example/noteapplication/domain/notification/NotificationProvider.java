package com.example.noteapplication.domain.notification;

public interface NotificationProvider {

    /**
     * <p> Function is responsible for creating notification of scheduled note with given parameters. </p>
     * @param id note's ID is used as ID of the notification to be shown
     * @param title note's title
     * @param text note's text
     **/
    void create(int id, String title, String text);
}
