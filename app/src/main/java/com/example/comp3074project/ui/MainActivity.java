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

        bottomNavigationView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            throw new IllegalStateException("NavHostFragment with id R.id.nav_host_fragment not found");
        }

        navController = navHostFragment.getNavController();

        // Connect BottomNavigationView with NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Handle reselecting the same item
        bottomNavigationView.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // Try to get the current fragment
                Fragment currentFragment = getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment)
                        .getChildFragmentManager()
                        .getFragments()
                        .get(0);

                if (currentFragment instanceof HomeFragment) {
                    // Call a method in HomeFragment to refresh or scroll to top
                    ((HomeFragment) currentFragment).scrollToTopOrRefresh();
                } else {
                    // Navigate explicitly to HomeFragment if not already there
                    navController.navigate(R.id.navigation_home);
                }
            }
        });
    }
}
