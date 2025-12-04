package com.example.comp3074project.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewReminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ReminderAdapter();
        recyclerView.setAdapter(adapter);

        tvActiveReminders = view.findViewById(R.id.tvActiveReminders);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Observe LiveData
        reminderViewModel.getAllReminders().observe(getViewLifecycleOwner(), this::updateUI);

        // Setup Edit/Delete click handling
        adapter.setOnReminderClickListener(new ReminderAdapter.OnReminderClickListener() {
            @Override
            public void onEdit(ReminderEntity reminder) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("reminder", reminder);

                // Correct action ID from nav_graph.xml
                NavHostFragment.findNavController(RemindersFragment.this)
                        .navigate(R.id.action_navigation_reminders_to_addEditReminder, bundle);
            }

            @Override
            public void onDelete(ReminderEntity reminder) {
                reminderViewModel.delete(reminder);
                Toast.makeText(getContext(), "Reminder deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // Add new reminder button
        Button btnAddNewReminder = view.findViewById(R.id.btn_add_new_reminder);
        btnAddNewReminder.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.addEditReminderFragment)
        );
    }

    private void updateUI(List<ReminderEntity> reminders) {
        adapter.setReminders(reminders);

        if (tvActiveReminders != null) {
            tvActiveReminders.setText(reminders.size() + " active reminder" + (reminders.size() != 1 ? "s" : ""));
        }
    }
}
