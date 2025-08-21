package com.example.noteapplication.di;

import com.example.noteapplication.data.notification.NotificationReceiver;
import com.example.noteapplication.di.modules.BinderModule;
import com.example.noteapplication.di.modules.DatabaseModule;
import com.example.noteapplication.di.subcomponents.MainActivitySubcomponent;
import com.example.noteapplication.di.subcomponents.NoteListFragmentSubcomponent;
import com.example.noteapplication.di.subcomponents.NotificationReceiverSubcomponent;
import com.example.noteapplication.ui.MainActivity;
import com.example.noteapplication.ui.note_list_screen.NoteListFragment;
import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module(
        includes = { DatabaseModule.class, BinderModule.class },
        subcomponents = { MainActivitySubcomponent.class, NoteListFragmentSubcomponent.class, NotificationReceiverSubcomponent.class }
)
public abstract class AppModule {

    @Binds
    @IntoMap
    @ClassKey(MainActivity.class)
    abstract AndroidInjector.Factory<?> bindMainActivityInjectorFactory(MainActivitySubcomponent.Factory factory);

    @Binds
    @IntoMap
    @ClassKey(NoteListFragment.class)
    abstract AndroidInjector.Factory<?> bindNoteListFragmentInjectorFactory(NoteListFragmentSubcomponent.Factory factory);

    @Binds
    @IntoMap
    @ClassKey(NotificationReceiver.class)
    abstract AndroidInjector.Factory<?> bindNotificationReceiverInjectorFactory(NotificationReceiverSubcomponent.Factory factory);
}
