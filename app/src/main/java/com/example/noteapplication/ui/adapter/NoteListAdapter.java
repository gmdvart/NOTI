package com.example.noteapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.databinding.NoteListItemBinding;
import com.example.noteapplication.utils.NoteUtils;
import org.jetbrains.annotations.NotNull;

public class NoteListAdapter extends ListAdapter<Note, NoteListAdapter.NoteViewHolder> {
    private OnNoteClickListener onNoteClickListener;

    public NoteListAdapter(OnNoteClickListener onNoteClickListener) {
        super(NoteListAdapter.DiffCallback);
        this.onNoteClickListener = onNoteClickListener;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final NoteListItemBinding binding;

        NoteViewHolder(NoteListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Note note) {
            binding.noteIndicator.setImageResource(NoteUtils.ImportanceSelection.getImageResourceForImportanceByLevel(note.importanceLevel));
            if (note.notificationDate != 0)
                binding.noteNotifyDate.setText(NoteUtils.DateManipulator.getFormattedFullDate(note.notificationDate));
            binding.noteTitle.setText(note.title);
            binding.noteDescription.setText(note.description);
            binding.noteCreationDate.setText(note.creationDate);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        NoteListItemBinding binding = NoteListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding);
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
