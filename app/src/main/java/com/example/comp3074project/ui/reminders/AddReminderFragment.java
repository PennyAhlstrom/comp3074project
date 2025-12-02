package com.example.comp3074project.ui.reminders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.ReminderEntity;
import com.example.comp3074project.viewModel.ReminderViewModel;

import java.util.Calendar;

public class AddReminderFragment extends Fragment {

    private ReminderViewModel reminderViewModel;

    private EditText etReminderTitle, etAlertDate, etAlertTime, etNote;
    private Spinner spinnerTask, spinnerCourse, spinnerPriority;
    private Button btnSave, btnCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);

        // Initialize ViewModel
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Bind views
        etReminderTitle = view.findViewById(R.id.etReminderTitle);
        etAlertDate = view.findViewById(R.id.etAlertDate);
        etAlertTime = view.findViewById(R.id.etAlertTime);
        etNote = view.findViewById(R.id.etNote);

        spinnerTask = view.findViewById(R.id.spinnerTask);
        spinnerCourse = view.findViewById(R.id.spinnerCourse);
        spinnerPriority = view.findViewById(R.id.spinnerPriority);

        btnSave = view.findViewById(R.id.btn_save_reminder);
        btnCancel = view.findViewById(R.id.btn_cancel);

        setupSpinners();
        setupDatePicker();
        setupTimePicker();
        setupButtons();

        return view;
    }

    private void setupSpinners() {
        // Sample data for spinners
        String[] tasks = {"Assignment 1", "Assignment 2", "Midterm Review"};
        String[] courses = {"COMP3074", "COMP3095", "COMP3080"};
        String[] priorities = {"High", "Medium", "Low"};

        spinnerTask.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, tasks));

        spinnerCourse.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, courses));

        spinnerPriority.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, priorities));
    }

    private void setupDatePicker() {
        etAlertDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) ->
                            etAlertDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setupTimePicker() {
        etAlertTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(requireContext(),
                    (view, hourOfDay, minute) ->
                            etAlertTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true).show(); // true = 24-hour format
        });
    }

    private void setupButtons() {
        btnSave.setOnClickListener(v -> saveReminder());
        btnCancel.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }

    private void saveReminder() {
        String title = etReminderTitle.getText().toString().trim();
        String task = spinnerTask.getSelectedItem() != null ? spinnerTask.getSelectedItem().toString() : "";
        String course = spinnerCourse.getSelectedItem() != null ? spinnerCourse.getSelectedItem().toString() : "";
        String priority = spinnerPriority.getSelectedItem() != null ? spinnerPriority.getSelectedItem().toString() : "";
        String date = etAlertDate.getText().toString().trim();
        String time = etAlertTime.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        // Full validation for all required fields
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(task) || TextUtils.isEmpty(course)
                || TextUtils.isEmpty(priority) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create ReminderEntity and insert into Room database
        ReminderEntity reminder = new ReminderEntity(title, task, course, priority, date, time, note);
        reminderViewModel.insert(reminder);

        Toast.makeText(getContext(), "Reminder saved", Toast.LENGTH_SHORT).show();

        // Navigate back to RemindersFragment
        NavHostFragment.findNavController(this).popBackStack();
    }
}
