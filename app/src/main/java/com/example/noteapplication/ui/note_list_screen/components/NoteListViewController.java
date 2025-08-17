package com.example.noteapplication.ui.note_list_screen.components;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.noteapplication.R;
import com.example.noteapplication.data.database.Note;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NoteListViewController {

    private final RecyclerView recyclerView;
    private final NoteListAdapter.OnNoteClickListener onNoteClickListener;
    private NoteListAdapter adapter;


    public NoteListViewController(RecyclerView recyclerView, NoteListAdapter.OnNoteClickListener onNoteClickListener, boolean isGridLayoutEnabled) {
        this.recyclerView = recyclerView;
        this.onNoteClickListener = onNoteClickListener;

        setUpRecyclerView(isGridLayoutEnabled);
    }

    private void setUpRecyclerView(boolean isGridLayoutEnabled) {
        recyclerView.setPadding(12, 0, 12, 12);
        setUpLayoutDisplayType(isGridLayoutEnabled);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
                outRect.set(8, 8, 8, 8);
            }
        });

        adapter = new NoteListAdapter(onNoteClickListener);
        recyclerView.setAdapter(adapter);
    }

    public void setUpLayoutDisplayType(boolean isGridLayoutEnabled) {
        Context context = recyclerView.getContext();

        if (isGridLayoutEnabled) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        Animation layoutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        LayoutAnimationController controller = new LayoutAnimationController(layoutAnimation);
        recyclerView.setLayoutAnimation(controller);
    }

    public void resetScrollPosition() {
        recyclerView.scrollToPosition(0);
    }

    public void submitList(List<Note> noteList) {
        adapter.submitList(noteList);
    }
}
