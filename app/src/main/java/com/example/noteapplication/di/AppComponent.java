package com.example.noteapplication.di;

import android.content.Context;
import com.example.noteapplication.NoteApplication;
import com.example.noteapplication.ui.note_list_screen.NoteListFragment;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

@Component(modules = { AndroidInjectionModule.class, AppModule.class })
@Singleton
public interface AppComponent {

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Context context);
    }

    void inject(NoteApplication application);
}
