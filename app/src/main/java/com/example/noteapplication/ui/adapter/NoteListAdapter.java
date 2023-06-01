package com.example.noteapplication.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.noteapplication.R;
import com.example.noteapplication.constants.NoteNotificationsKeys;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.databinding.NoteListItemBinding;
import com.example.noteapplication.utils.NoteUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NoteListAdapter extends ListAdapter<Note, NoteListAdapter.NoteViewHolder> {
    private final OnNoteClickListener onNoteClickListener;

    public NoteListAdapter(OnNoteClickListener onNoteClickListener) {
        super(NoteListAdapter.DiffCallback);
        this.onNoteClickListener = onNoteClickListener;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final NoteListItemBinding binding;
        private final Context context;

        NoteViewHolder(Context context, NoteListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        public void bind(Note note) {
            binding.noteIndicator.setImageResource(
                    NoteUtils.ImportanceSelection.getImageResourceForImportanceByLevel(
                            NoteUtils.ImportanceSelection.getImportanceLevel(note.importanceLevel)
                    )
            );

            if (note.notificationDate != NoteNotificationsKeys.WITHOUT_NOTIFICATION) {
                String formattedNotificationDate = NoteUtils.DateManipulator.getFormattedDisplayedDate(context, note.notificationDate * 1000L);
                String notificationText = context.getString(R.string.notify_on, formattedNotificationDate);
                binding.noteNotifyDate.setText(notificationText);
                binding.noteNotifyDate.setVisibility(View.VISIBLE);
            } else
                binding.noteNotifyDate.setVisibility(View.GONE);

            binding.noteTitle.setText(note.title);
            binding.noteDescription.setText(note.description);

            String formattedCreationDate = NoteUtils.DateManipulator.getFormattedDisplayedDate(context, note.creationDate * 1000L);
            String creationText = context.getString(R.string.created_at, formattedCreationDate);
            binding.noteCreationDate.setText(creationText);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        NoteListItemBinding binding = NoteListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(parent.getContext(), binding);
    }

    @Override
    public void onBindViewHolder(@NotNull NoteViewHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNoteClickListener.onClick(currentNote.id);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onNoteClickListener.onLongClick(currentNote, view);
                return true;
            }
        });
        holder.bind(currentNote);
    }

    static final DiffUtil.ItemCallback<Note> DiffCallback = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NotNull Note oldItem, @NotNull Note newItem) {
            return oldItem.title.equals(newItem.title) && oldItem.description.equals(newItem.description);
        }
    };

    public interface OnNoteClickListener {
        void onClick(int noteId);

        void onLongClick(Note note, View anchor);
    }
}
