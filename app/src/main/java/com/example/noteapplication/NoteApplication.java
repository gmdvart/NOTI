package com.example.noteapplication;

import android.app.Application;
import com.example.noteapplication.data.app.AppDataContainer;

public class NoteApplication extends Application {
    private AppDataContainer container;
    public AppDataContainer getContainer() {
        return container;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        container = new AppDataContainer(this);
    }
}
