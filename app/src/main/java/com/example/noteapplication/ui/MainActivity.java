package com.example.noteapplication.ui;

import android.Manifest;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.noteapplication.R;
import com.example.noteapplication.databinding.ActivityMainBinding;
import com.example.noteapplication.ui.note_edit_screen.NoteEditViewModel;

public class MainActivity extends AppCompatActivity {
    private NavController _navController;
    private NoteEditViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(ActivityMainBinding.inflate(getLayoutInflater()).getRoot());

        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(NoteEditViewModel.initializer)).get(NoteEditViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        _navController = navHostFragment.getNavController();

        requestPermissions();
    }

    private void requestPermissions() {
        ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isPermitted -> {
                    if (!isPermitted)
                        Toast.makeText(this, getString(R.string.notification_permission_required), Toast.LENGTH_SHORT).show();
                }
        );
        activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }

    @Override
    public boolean onNavigateUp() {
        return _navController.navigateUp() || super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        viewModel.onEvent(new NoteEditViewModel.Event.NavigateBack());
        super.onBackPressed();
    }
}