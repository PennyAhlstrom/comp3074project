package com.example.comp3074project.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.comp3074project.R;
import com.example.comp3074project.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the BottomNavigationView
        bottomNavigationView = findViewById(R.id.nav_view);

        // Find the NavHostFragment and NavController
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment not found");
        }

        navController = navHostFragment.getNavController();

        // Connect BottomNavigationView with NavController for normal navigation
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Handle reselection of the same item (e.g., scroll/refresh Home)
        bottomNavigationView.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // Get the currently displayed fragment reliably
                Fragment currentFragment = navHostFragment
                        .getChildFragmentManager()
                        .getPrimaryNavigationFragment();

                if (currentFragment instanceof HomeFragment) {
                    // Call HomeFragment's method to scroll or refresh
                    ((HomeFragment) currentFragment).scrollToTopOrRefresh();
                }
            }
        });
    }
}
