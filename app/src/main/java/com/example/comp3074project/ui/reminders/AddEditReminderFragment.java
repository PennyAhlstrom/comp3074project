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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.ReminderEntity;
import com.example.comp3074project.entity.TaskEntity;
import com.example.comp3074project.entity.CourseEntity;
import com.example.comp3074project.viewModel.ReminderViewModel;
import com.example.comp3074project.viewModel.TaskViewModel;
import com.example.comp3074project.viewModel.CourseViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AddEditReminderFragment extends Fragment {

    private EditText etTitle, etNote, etAlertDate, etAlertTime;
    private Spinner spinnerTask, spinnerCourse, spinnerPriority;
    private Button btnSave, btnCancel;

    private TaskViewModel taskViewModel;
    private CourseViewModel courseViewModel;
    private ReminderViewModel reminderViewModel;

    private Integer selectedTaskId = null;
    private Integer selectedCourseId = null;
    private ReminderEntity editingReminder = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        etTitle = view.findViewById(R.id.etReminderTitle);
        etNote = view.findViewById(R.id.etNote);
        etAlertDate = view.findViewById(R.id.etAlertDate);
        etAlertTime = view.findViewById(R.id.etAlertTime);
        spinnerTask = view.findViewById(R.id.spinnerTask);
        spinnerCourse = view.findViewById(R.id.spinnerCourse);
        spinnerPriority = view.findViewById(R.id.spinnerPriority);
        btnSave = view.findViewById(R.id.btn_save_reminder);
        btnCancel = view.findViewById(R.id.btn_cancel);

        // Initialize ViewModels
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        reminderViewModel = new ViewModelProvider(requireActivity()).get(ReminderViewModel.class);

        // Setup dropdowns and pickers
        setupTaskDropdown();
        setupCourseDropdown();
        setupPriorityDropdown();
        setupDatePicker();
        setupTimePicker();

        // Check if editing an existing reminder
        if (getArguments() != null && getArguments().containsKey("reminder")) {
            editingReminder = (ReminderEntity) getArguments().getSerializable("reminder");
            populateFieldsForEditing();
        }

        btnSave.setOnClickListener(v -> saveReminder());
        btnCancel.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }

    private void setupTaskDropdown() {
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {

            // No tasks available → show placeholder + block selection
            if (tasks == null || tasks.isEmpty()) {
                List<String> placeholder = new ArrayList<>();
                placeholder.add("No tasks available - please add a task first");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        placeholder
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTask.setAdapter(adapter);

                spinnerTask.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Toast.makeText(
                                requireContext(),
                                "You must add at least one task before assigning it to a reminder.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                    return true; // Prevent opening dropdown
                });

                selectedTaskId = null;
                return;
            }

            // Tasks exist → normal behavior
            List<String> taskTitles = new ArrayList<>();
            for (TaskEntity t : tasks) {
                taskTitles.add(t.getTitle());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    taskTitles
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTask.setAdapter(adapter);

            spinnerTask.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                    selectedTaskId = tasks.get(position).getId();
                }

                @Override
                public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            });

            // Pre-select when editing
            if (editingReminder != null && editingReminder.getTaskId() != null) {
                for (int i = 0; i < tasks.size(); i++) {
                    if (Objects.equals(tasks.get(i).getId(), editingReminder.getTaskId())) {
                        spinnerTask.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void setupCourseDropdown() {
        courseViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            List<String> courseNames = new ArrayList<>();
            for (CourseEntity course : courses) {
                courseNames.add(course.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, courseNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCourse.setAdapter(adapter);

            spinnerCourse.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                    selectedCourseId = courses.get(position).getId();
                }

                @Override
                public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            });

            // Pre-select current course if editing
            if (editingReminder != null && editingReminder.getCourseId() != null) {
                for (int i = 0; i < courses.size(); i++) {
                    if (Objects.equals(courses.get(i).getId(), editingReminder.getCourseId())) {
                        spinnerCourse.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void setupPriorityDropdown() {
        String[] priorities = {"Low", "Medium", "High"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, priorities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        // Pre-select priority if editing
        if (editingReminder != null && editingReminder.priority != null) {
            int index = java.util.Arrays.asList(priorities).indexOf(editingReminder.priority);
            spinnerPriority.setSelection(index >= 0 ? index : 0);
        }
    }

    private void setupDatePicker() {
        etAlertDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) ->
                            etAlertDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        });
    }

    private void setupTimePicker() {
        etAlertTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(requireContext(),
                    (view, hourOfDay, minute) ->
                            etAlertTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                    .show();
        });
    }

    private void populateFieldsForEditing() {
        if (editingReminder != null) {
            etTitle.setText(editingReminder.title);
            etNote.setText(editingReminder.note);
            etAlertDate.setText(editingReminder.alertDate);
            etAlertTime.setText(editingReminder.alertTime);
        }
    }

    private void saveReminder() {
        String title = etTitle.getText().toString().trim();
        String note = etNote.getText().toString().trim();
        String alertDate = etAlertDate.getText().toString().trim();
        String alertTime = etAlertTime.getText().toString().trim();
        String priority = spinnerPriority.getSelectedItem().toString();

        if (TextUtils.isEmpty(title) || selectedTaskId == null || selectedCourseId == null) {
            Toast.makeText(requireContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editingReminder == null) {
            // Add new reminder
            ReminderEntity reminder = new ReminderEntity(title, "", "", priority,
                    alertDate, alertTime, note, selectedTaskId, selectedCourseId);
            reminderViewModel.insert(reminder);
        } else {
            // Update existing reminder
            editingReminder.title = title;
            editingReminder.note = note;
            editingReminder.alertDate = alertDate;
            editingReminder.alertTime = alertTime;
            editingReminder.priority = priority;
            editingReminder.setTaskId(selectedTaskId);
            editingReminder.setCourseId(selectedCourseId);
            reminderViewModel.update(editingReminder);
        }

        Toast.makeText(requireContext(), "Reminder saved", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).popBackStack();
    }
}
