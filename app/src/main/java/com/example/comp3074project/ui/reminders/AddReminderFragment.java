package com.example.comp3074project.ui.reminders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comp3074project.entity.ReminderEntity;
import com.example.comp3074project.viewModel.ReminderViewModel;
import com.example.comp3074project.R;

public class AddReminderFragment extends Fragment {

    private ReminderViewModel reminderViewModel;

    private EditText etTitle, etAlertDate, etAlertTime, etNote;
    private Spinner spinnerTask, spinnerCourse, spinnerPriority;

    public AddReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Initialize views
        etTitle = view.findViewById(R.id.etReminderTitle);
        etAlertDate = view.findViewById(R.id.etAlertDate);
        etAlertTime = view.findViewById(R.id.etAlertTime);
        etNote = view.findViewById(R.id.etNote);

        spinnerTask = view.findViewById(R.id.spinnerTask);
        spinnerCourse = view.findViewById(R.id.spinnerCourse);
        spinnerPriority = view.findViewById(R.id.spinnerPriority);

        Button cancelButton = view.findViewById(R.id.btn_cancel);
        Button saveButton = view.findViewById(R.id.btn_save_reminder);

        // Cancel button navigates back
        cancelButton.setOnClickListener(v ->
                NavHostFragment.findNavController(AddReminderFragment.this).popBackStack()
        );

        // Save button validates and inserts reminder
        saveButton.setOnClickListener(v -> saveReminder());
    }

    private void saveReminder() {
        String title = etTitle.getText().toString().trim();
        String task = spinnerTask.getSelectedItem() != null ? spinnerTask.getSelectedItem().toString() : "";
        String course = spinnerCourse.getSelectedItem() != null ? spinnerCourse.getSelectedItem().toString() : "";
        String priority = spinnerPriority.getSelectedItem() != null ? spinnerPriority.getSelectedItem().toString() : "";
        String date = etAlertDate.getText().toString().trim();
        String time = etAlertTime.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        // Simple form validation
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(task) || TextUtils.isEmpty(course)
                || TextUtils.isEmpty(priority) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create ReminderEntity and insert
        ReminderEntity reminder = new ReminderEntity(title, task, course, priority, date, time, note);
        reminderViewModel.insert(reminder);

        Toast.makeText(getContext(), "Reminder saved!", Toast.LENGTH_SHORT).show();

        // Navigate back to RemindersFragment
        NavHostFragment.findNavController(AddReminderFragment.this).popBackStack();
    }
}