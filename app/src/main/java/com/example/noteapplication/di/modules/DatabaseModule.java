package com.example.noteapplication.di.modules;

import android.content.Context;
import com.example.noteapplication.data.database.NoteDatabase;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class DatabaseModule {
    @Provides
    public static NoteDatabase provideNoteDatabase(Context context) {
        return NoteDatabase.getDatabase(context);
    }
}
