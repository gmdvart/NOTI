package com.example.noteapplication.ui.note_edit_screen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.noteapplication.R;
import com.example.noteapplication.databinding.FragmentNotePermissionBinding;
import org.jetbrains.annotations.NotNull;

public class NotePermissionDialogFragment extends DialogFragment {

    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        FragmentNotePermissionBinding _binding = FragmentNotePermissionBinding.inflate(inflater, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.notification_permission_title)
                .setView(_binding.getRoot())
                .setNegativeButton(R.string.notification_permission_deny, null)
                .setPositiveButton(R.string.notification_permission_allow, (dialog, which) -> openSettingsActivity());

        return builder.create();
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        String packageName = requireContext().getPackageName();
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        startActivity(intent);
    }
}
