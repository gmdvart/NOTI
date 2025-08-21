package com.example.noteapplication.di.subcomponents;

import com.example.noteapplication.data.notification.NotificationReceiver;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface NotificationReceiverSubcomponent extends AndroidInjector<NotificationReceiver> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<NotificationReceiver> {  }
}
