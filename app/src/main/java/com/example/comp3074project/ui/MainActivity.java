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
            throw new IllegalStateException("NavHostFragment not found");
        }

        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

            if (handled && item.getItemId() == R.id.navigation_home) {
                navController.popBackStack(R.id.navigation_home, false);
            }

            return handled;
        });

        bottomNavigationView.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                Fragment currentFragment = navHostFragment
                        .getChildFragmentManager()
                        .getPrimaryNavigationFragment();

                if (currentFragment instanceof HomeFragment) {
                    ((HomeFragment) currentFragment).scrollToTopOrRefresh();
                }
            }
        });
    }
}
