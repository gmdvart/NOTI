package com.example.noteapplication;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import com.example.noteapplication.databinding.FragmentNoteEditBinding;
import org.jetbrains.annotations.NotNull;

public class NoteEditFragment extends Fragment implements MenuProvider {
    private FragmentNoteEditBinding _binding;
    public FragmentNoteEditBinding getBinding() {
        return _binding;
    }
    private NavController navController;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MenuHost) requireActivity()).addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(_binding.editToolbar);

        _binding.editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackArrowClick();
            }
        });
    }

    private void onBackArrowClick() {
        NavDirections action = NoteEditFragmentDirections.actionNoteEditFragmentToNoteListFragment();
        navController.navigate(action);
    }

    @Override
    public void onCreateMenu(@NotNull Menu menu, @NotNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.edit_note_toolbar_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NotNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.edit_done_item:
                Toast.makeText(requireActivity(), "Clicked on Done", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.edit_notify_item:
                Toast.makeText(requireActivity(), "Clicked on Notification", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.edit_importance_item:
                Toast.makeText(requireActivity(), "Clicked on Importance", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}