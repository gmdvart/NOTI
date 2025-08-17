package com.example.noteapplication.ui.note_list_screen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import com.example.noteapplication.R;
import com.example.noteapplication.ui.constants.NoteFilterKeys;
import com.example.noteapplication.ui.constants.NotePreferences;
import com.example.noteapplication.data.database.Note;
import com.example.noteapplication.databinding.FragmentNoteListBinding;
import com.example.noteapplication.ui.note_list_screen.components.NoteListAdapter;
import com.example.noteapplication.ui.note_list_screen.components.NoteListViewController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

public class NoteListFragment extends Fragment implements MenuProvider, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "NoteListFragment";

    private FragmentNoteListBinding _binding;
    public FragmentNoteListBinding getBinding() {
        return _binding;
    }

    private NavController navController;

    private NoteListViewModel viewModel;

    private boolean isGridLayoutEnabled;
    private SharedPreferences.Editor layoutPreferencesEditor;

    private NoteListViewController noteListViewController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences layoutPreferences = requireActivity().getSharedPreferences(NotePreferences.LAYOUT_PREFERENCES_NAME, Context.MODE_PRIVATE);
        isGridLayoutEnabled = layoutPreferences.getBoolean(NotePreferences.LAYOUT_TYPE_KEY, false);
        layoutPreferencesEditor = layoutPreferences.edit();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(NoteListViewModel.initializer)).get(NoteListViewModel.class);

        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MenuHost) requireActivity()).addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(_binding.toolbar);

        setupChipMenu();
        setUpNoteViewController();
        setupFab();
    }

    private void setupChipMenu() {
        _binding.filterChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> chooseFilter(group.getCheckedChipId()));
    }

    private void chooseFilter(int chipId) {
        switch (chipId) {
            case R.id.importance_chip:
                viewModel.onEvent(new NoteListViewModel.Event.FilterKeyChanged(NoteFilterKeys.BY_IMPORTANCE));
                break;
            case R.id.notification_chip:
                viewModel.onEvent(new NoteListViewModel.Event.FilterKeyChanged(NoteFilterKeys.BY_NOTIFICATION));
                break;
            case R.id.creation_date_chip:
                viewModel.onEvent(new NoteListViewModel.Event.FilterKeyChanged(NoteFilterKeys.BY_CREATION));
                break;
            case R.id.alphabet_chip:
                viewModel.onEvent(new NoteListViewModel.Event.FilterKeyChanged(NoteFilterKeys.BY_ALPHABET));
                break;
            default:
                viewModel.onEvent(new NoteListViewModel.Event.FilterKeyChanged(NoteFilterKeys.DEFAULT));
                break;
        }
    }

    private void setUpNoteViewController() {
        noteListViewController = new NoteListViewController(_binding.recyclerView, new NoteListAdapter.OnNoteClickListener() {
            @Override
            public void onClick(int noteId) {
                navigateToNoteWithId(noteId);
            }

            @Override
            public void onLongClick(Note note, View anchor) {
                viewModel.onEvent(new NoteListViewModel.Event.LongClickNoteChanged(note));
                showPopupMenu(anchor);
            }
        }, isGridLayoutEnabled);

        viewModel.noteList.observe(getViewLifecycleOwner(), noteListViewController::submitList);
    }

    private void navigateToNoteWithId(int noteId) {
        NoteListFragmentDirections.ActionNoteListFragmentToNoteEditFragment toNoteEditScreenWithId = NoteListFragmentDirections.actionNoteListFragmentToNoteEditFragment();
        toNoteEditScreenWithId.setNoteId(noteId);
        navController.navigate(toNoteEditScreenWithId);
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        popupMenu.inflate(R.menu.note_popup_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }


    private void setupFab() {
        _binding.addButton.setOnClickListener(view -> {
            NoteListFragmentDirections.ActionNoteListFragmentToNoteEditFragment toNoteEditScreen =
                    NoteListFragmentDirections.actionNoteListFragmentToNoteEditFragment();
            navController.navigate(toNoteEditScreen);
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
                viewModel.onEvent(new NoteListViewModel.Event.SearchQueryChanged(newText));
                return true;
            }
        });
        setIcon(menu.findItem(R.id.list_layout_item));
    }

    @Override
    public boolean onMenuItemSelected(@NotNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.list_layout_item) {
            isGridLayoutEnabled = !isGridLayoutEnabled;
            noteListViewController.setUpLayoutDisplayType(isGridLayoutEnabled);
            savePreferences();
            setIcon(menuItem);
            return true;
        } else {
            return false;
        }
    }

    private void savePreferences() {
        layoutPreferencesEditor.putBoolean(NotePreferences.LAYOUT_TYPE_KEY, isGridLayoutEnabled);
        layoutPreferencesEditor.apply();
    }

    private void setIcon(MenuItem menuItem) {
        if (isGridLayoutEnabled) menuItem.setIcon(R.drawable.ic_grid);
        else menuItem.setIcon(R.drawable.ic_list);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        viewModel.onEvent(new NoteListViewModel.Event.DeleteNote());
        return true;
    }
}