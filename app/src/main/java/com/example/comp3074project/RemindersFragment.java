package com.example.comp3074project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

// This fragment shows the reminders list screen
public class RemindersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminders, container, false);

        // Find the "Add New Reminder" button
        Button addReminderButton = view.findViewById(R.id.btn_add_new_reminder);

        // Navigate to the AddReminder screen when clicked
        addReminderButton.setOnClickListener(v -> {
            NavController navController =
                    NavHostFragment.findNavController(RemindersFragment.this);
            // Navigate directly to the destination ID in the nav_graph
            navController.navigate(R.id.addReminderFragment);
        });

        return view;
    }
}
