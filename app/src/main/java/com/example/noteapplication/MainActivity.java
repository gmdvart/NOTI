package com.example.noteapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.noteapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private NavController _navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

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