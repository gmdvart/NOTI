package com.example.noteapplication;

import android.app.Application;
import com.example.noteapplication.di.ApplicationComponent;
import com.example.noteapplication.di.DefaultApplicationComponent;

public class NoteApplication extends Application {

    private ApplicationComponent container;
    public ApplicationComponent getAppComponent() {
        return container;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        container = new DefaultApplicationComponent(this);
    }
}
