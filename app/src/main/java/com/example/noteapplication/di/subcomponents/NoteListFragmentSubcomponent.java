package com.example.noteapplication.di.subcomponents;

import com.example.noteapplication.ui.note_list_screen.NoteListFragment;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface NoteListFragmentSubcomponent extends AndroidInjector<NoteListFragment> {

    @Subcomponent.Factory
    interface Factory extends AndroidInjector.Factory<NoteListFragment> {  }
}
