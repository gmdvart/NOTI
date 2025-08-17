package com.example.noteapplication.ui.note_list_screen.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.databinding.NoteListItemBinding;
import com.example.noteapplication.ui.utils.NoteUtils;
import org.jetbrains.annotations.NotNull;

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
                        note.importanceLevel
                    )
            );

            if (note.notificationDate != 0L) {
                binding.noteNotifyDate.setText(NoteUtils.DateManipulator.getForamttedNotificationDateString(context, note.notificationDate));
                binding.noteNotifyDate.setVisibility(View.VISIBLE);
            } else {
                binding.noteNotifyDate.setVisibility(View.GONE);
            }

            binding.noteTitle.setText(note.title);
            binding.noteDescription.setText(note.description);
            binding.noteCreationDate.setText(NoteUtils.DateManipulator.getForamttedCreationDateString(context, note.creationDate));
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
        holder.itemView.setOnClickListener(view -> onNoteClickListener.onClick(currentNote.id));
        holder.itemView.setOnLongClickListener(view -> {
            onNoteClickListener.onLongClick(currentNote, view);
            return true;
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
            return oldItem.title.equals(newItem.title)
                    && oldItem.description.equals(newItem.description)
                    && oldItem.notificationDate == newItem.notificationDate;
        }
    };

    public interface OnNoteClickListener {
        void onClick(int noteId);
        void onLongClick(Note note, View anchor);
    }
}
