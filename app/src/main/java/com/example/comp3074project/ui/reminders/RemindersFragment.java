package com.example.comp3074project.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.ReminderEntity;
import com.example.comp3074project.viewModel.ReminderViewModel;

import java.util.List;

public class RemindersFragment extends Fragment {

    private ReminderViewModel reminderViewModel;
    private ReminderAdapter adapter;
    private TextView tvActiveReminders;

    public RemindersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewReminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReminderAdapter();
        recyclerView.setAdapter(adapter);

        // Active reminders count
        tvActiveReminders = view.findViewById(R.id.tvActiveReminders);

        // Initialize ViewModel
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Observe LiveData from ViewModel
        reminderViewModel.getAllReminders().observe(getViewLifecycleOwner(), this::updateUI);

        // Add New Reminder button listener
        Button btnAddNewReminder = view.findViewById(R.id.btn_add_new_reminder);
        btnAddNewReminder.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_navigation_reminders_to_addReminderFragment)
        );
    }

    private void updateUI(List<ReminderEntity> reminders) {
        // Update RecyclerView
        adapter.setReminders(reminders);

        // Update active reminders count
        if (tvActiveReminders != null) {
            tvActiveReminders.setText(reminders.size() + " active reminder" + (reminders.size() != 1 ? "s" : ""));
        }
    }
}
