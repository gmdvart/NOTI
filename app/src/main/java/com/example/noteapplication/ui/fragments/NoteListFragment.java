package com.example.noteapplication.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.noteapplication.R;
import com.example.noteapplication.constants.NoteFilterKeys;
import com.example.noteapplication.constants.NotePreferences;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.databinding.FragmentNoteListBinding;
import com.example.noteapplication.ui.NoteViewModel;
import com.example.noteapplication.ui.adapter.NoteListAdapter;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NoteListFragment extends Fragment implements MenuProvider, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "NoteListFragment";

    private FragmentNoteListBinding _binding;
    public FragmentNoteListBinding getBinding() {
        return _binding;
    }
    private NavController navController;

    private NoteViewModel viewModel;
    private Note noteToDelete;

    private boolean isLinearLayoutSelected;
    private SharedPreferences.Editor layoutPreferencesEditor;

    private NoteListAdapter adapter;

    private int lastSelectedFilter = NoteFilterKeys.DEFAULT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences layoutPreferences = requireActivity().getSharedPreferences(NotePreferences.LAYOUT_PREFERENCES_NAME, Context.MODE_PRIVATE);
        isLinearLayoutSelected = layoutPreferences.getBoolean(NotePreferences.LAYOUT_TYPE_KEY, true);
        layoutPreferencesEditor = layoutPreferences.edit();
    }

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
        setupChipMenu();
    }

    private void chooseLayout() {

    }

    private void setIcon(MenuItem menuItem) {
        if (isLinearLayoutSelected) menuItem.setIcon(R.drawable.ic_grid);
        else menuItem.setIcon(R.drawable.ic_list);
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
        adapter = new NoteListAdapter(new NoteListAdapter.OnNoteClickListener() {
            @Override
            public void onClick(int noteId) {
                navigateToNoteWithId(noteId);
            }

            @Override
            public void onLongClick(Note note, View anchor) {
                noteToDelete = note;
                showPopupMenu(anchor);
            }
        });
        _binding.recyclerView.setAdapter(adapter);
        setFilter(NoteFilterKeys.DEFAULT);
    }

    private void navigateToNoteWithId(int id) {
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

    private void setupChipMenu() {
        _binding.filterChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NotNull ChipGroup group, @NotNull List<Integer> checkedIds) {
                chooseFilter(group.getCheckedChipId());
            }
        });
    }

    private void chooseFilter(int chipId) {
        switch (chipId) {
            case R.id.importance_chip:
                lastSelectedFilter = NoteFilterKeys.BY_IMPORTANCE;
                break;
            case R.id.notification_chip:
                lastSelectedFilter = NoteFilterKeys.BY_NOTIFICATION;
                break;
            case R.id.creation_date_chip:
                lastSelectedFilter = NoteFilterKeys.BY_CREATION;
                break;
            case R.id.alphabet_chip:
                lastSelectedFilter = NoteFilterKeys.BY_ALPHABET;
                break;
            default:
                lastSelectedFilter = NoteFilterKeys.DEFAULT;
                break;
        }
        setFilter(lastSelectedFilter);
    }

    private void setFilter(int filterKey) {
        _binding.recyclerView.scrollToPosition(0);
        viewModel.readFilteredNotes(filterKey).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });
    }

    @Override
    public void onCreateMenu(@NotNull Menu menu, @NotNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.list_note_toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.list_search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            final MenuItem layoutItem = menu.findItem(R.id.list_layout_item);
            final LinearLayout filterBlock = _binding.filterBlock;
            final FloatingActionButton fab = _binding.addButton;

            @Override
            public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
                layoutItem.setVisible(false);
                filterBlock.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
                layoutItem.setVisible(true);
                filterBlock.setVisibility(View.VISIBLE);
                setFilter(lastSelectedFilter);
                fab.setVisibility(View.VISIBLE);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchForNotes(newText);
                return true;
            }
        });
        setIcon(menu.findItem(R.id.list_layout_item));
    }

    private void searchForNotes(String searchQuery) {
        String query = "%" + searchQuery + "%";
        viewModel.searchForNotes(query).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });
    }

    @Override
    public boolean onMenuItemSelected(@NotNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.list_layout_item) {
            isLinearLayoutSelected = !isLinearLayoutSelected;
            savePreferences();
            setIcon(menuItem);
            return true;
        } else {
            return false;
        }
    }

    private void savePreferences() {
        layoutPreferencesEditor.putBoolean(NotePreferences.LAYOUT_TYPE_KEY, isLinearLayoutSelected);
        layoutPreferencesEditor.apply();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        viewModel.deleteNote(noteToDelete);
        return true;
    }
}