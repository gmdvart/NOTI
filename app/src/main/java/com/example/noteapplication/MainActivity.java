package com.example.noteapplication;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.MenuProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import com.example.noteapplication.databinding.ActivityMainBinding;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private NavController _navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        _navController = navHostFragment.getNavController();
    }

    @Override
    public boolean onNavigateUp() {
        return _navController.navigateUp() || super.onNavigateUp();
    }
}