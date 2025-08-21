package com.example.noteapplication.ui.note_edit_screen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.noteapplication.R;
import com.example.noteapplication.databinding.FragmentNoteEditBinding;
import com.example.noteapplication.ui.note_edit_screen.components.NoteImportanceSelectionAdapter;
import com.example.noteapplication.ui.utils.NoteUtils;
import org.jetbrains.annotations.NotNull;

public class NoteEditFragment extends Fragment implements MenuProvider {

    private FragmentNoteEditBinding _binding;

    private NavController navController;
    private NoteEditViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false);


        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        viewModel = new ViewModelProvider(requireActivity()).get(NoteEditViewModel.class);
        int noteId = NoteEditFragmentArgs.fromBundle(requireArguments()).getNoteId();
        viewModel.loadNodeById(noteId);

        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(_binding.editToolbar);

        setUpImportanceField();
        setUpNotificationDateField();
        setUpTitleField();
        setUpDescriptionField();

        _binding.editToolbar.setNavigationOnClickListener(view1 -> navigateBack());
    }

    private void setUpImportanceField() {
        AutoCompleteTextView importanceSelection = _binding.noteEditImportance;
        String[] importanceSelectionList = new String[]{getString(R.string.importance_none), getString(R.string.importance_low), getString(R.string.importance_medium), getString(R.string.importance_high)};
        NoteImportanceSelectionAdapter adapter = new NoteImportanceSelectionAdapter(requireContext(), importanceSelectionList);
        importanceSelection.setAdapter(adapter);
        importanceSelection.setOnItemClickListener((parent, view, position, id) -> {
            viewModel.onEvent(new NoteEditViewModel.Event.ImportanceLevelChanged(position));
        });

        viewModel.importanceLevel.observe(getViewLifecycleOwner(), level -> {
            int importanceStringRes = NoteUtils.ImportanceSelection.getStringResourceByImportanceLevel(level);
            _binding.noteEditImportance.setText(getString(importanceStringRes), false);
            _binding.noteMenuItemIndicator.setImageResource(NoteUtils.ImportanceSelection.getImageResourceForImportanceByLevel(level));
        });
    }

    private void setUpNotificationDateField() {
        _binding.noteEditNotificationDate.setOnClickListener(view -> {
            boolean isPermissionGranted = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
            DialogFragment dialog = isPermissionGranted ? new NoteNotificationDialogFragment() : new NotePermissionDialogFragment();
            dialog.show(requireActivity().getSupportFragmentManager(), dialog.getTag());
        });

        viewModel.notificationDateInMillis.observe(getViewLifecycleOwner(), notificationDateInMillis -> {
            String formattedNotificationDateString = NoteUtils.DateManipulator.getFormattedNotificationDataString(requireContext(), notificationDateInMillis);
            _binding.noteEditNotificationDate.setText(formattedNotificationDateString);
        });
        viewModel.notificationDateSetError.observe(getViewLifecycleOwner(), isError -> {
            _binding.noteEditNOtifcationDateLayout.setErrorEnabled(isError);
            _binding.noteEditNOtifcationDateLayout.setError(isError ? getString(R.string.notification_set_error) : null);
        });
    }

    private void setUpTitleField() {
        _binding.noteEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                viewModel.onEvent(new NoteEditViewModel.Event.TitleChanged(text.toString()));
            }
            @Override
            public void afterTextChanged(Editable text) { }
        });

        _binding.noteEditTitle.setText(viewModel.getTitle());
    }

    private void setUpDescriptionField() {
        _binding.noteEditDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                viewModel.onEvent(new NoteEditViewModel.Event.DescriptionChanged(text.toString()));
            }
            @Override
            public void afterTextChanged(Editable text) { }
        });

        _binding.noteEditDescription.setText(viewModel.getDescription());
    }

    private void navigateBack() {
        navController.navigateUp();
        viewModel.onEvent(new NoteEditViewModel.Event.NavigateBack());
    }

    @Override
    public void onCreateMenu(@NotNull Menu menu, @NotNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.edit_note_toolbar_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NotNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.edit_done_item) {
            if (!_binding.noteEditNOtifcationDateLayout.isErrorEnabled()) {
                viewModel.onEvent(new NoteEditViewModel.Event.WriteNote());
                navigateBack();
            }
            return true;
        } else {
            return false;
        }
    }
}