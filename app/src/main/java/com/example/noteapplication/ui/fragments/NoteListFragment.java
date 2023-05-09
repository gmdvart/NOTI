package com.example.noteapplication.ui.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
//import com.example.noteapplication.NoteListFragmentDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.noteapplication.R;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.databinding.FragmentNoteListBinding;
import com.example.noteapplication.ui.NoteViewModel;
import com.example.noteapplication.ui.adapter.NoteListAdapter;
import org.jetbrains.annotations.NotNull;

public class NoteListFragment extends Fragment implements MenuProvider, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "NoteListFragment";

    private FragmentNoteListBinding _binding;
    public FragmentNoteListBinding getBinding() {
        return _binding;
    }
    private NavController navController;

    private NoteViewModel viewModel;
    private Note noteToDelete;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(NoteViewModel.initializer)).get(NoteViewModel.class);

        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MenuHost) requireActivity()).addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(_binding.toolbar);

        setupFab();
        setupRecyclerView();
    }

    private void setupFab() {
        _binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNoteClick();
            }
        });
    }

    private void onAddNoteClick() {
        NavDirections direction = NoteListFragmentDirections.actionNoteListFragmentToNoteEditFragment();
        navController.navigate(direction);
    }

    private void setupRecyclerView() {
        _binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        _binding.recyclerView.setAdapter(new NoteListAdapter(new NoteListAdapter.OnNoteClickListener() {
            @Override
            public void onClick(int noteId) {
                navigateWithNoteId(noteId);
            }

            @Override
            public void onLongClick(Note note, View anchor) {
                noteToDelete = note;
                showPopupMenu(anchor);
            }
        }));
    }

    private void navigateWithNoteId(int id) {
        com.example.noteapplication.ui.fragments.NoteListFragmentDirections.ActionNoteListFragmentToNoteEditFragment action =
                NoteListFragmentDirections.actionNoteListFragmentToNoteEditFragment();
        action.setNoteId(id);
        navController.navigate(action);
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        popupMenu.inflate(R.menu.note_popup_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public void onCreateMenu(@NotNull Menu menu, @NotNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.list_note_toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.list_search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            final MenuItem layoutItem = menu.findItem(R.id.list_layout_item);
            @Override
            public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
                layoutItem.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
                layoutItem.setVisible(true);
                return true;
            }
        });
    }

    @Override
    public boolean onMenuItemSelected(@NotNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.list_layout_item) {
            Toast.makeText(requireContext(), "Clicked on Layout", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        viewModel.deleteNote(noteToDelete);
        return true;
    }
}