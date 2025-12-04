package com.example.comp3074project.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.TaskEntity;
import com.example.comp3074project.viewModel.TaskViewModel;

public class EditTaskFragment extends Fragment {

    private EditText etTitle, etDescription;
    private TaskViewModel viewModel;
    private int taskId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Reuse the Add Task layout
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // MATCH EXACT IDs FROM fragment_add_task.xml
        etTitle = view.findViewById(R.id.etTaskTitle);
        etDescription = view.findViewById(R.id.etTaskDescription);
        Button btnSave = view.findViewById(R.id.btnSaveTask);

        // Update button label for clarity (optional)
        btnSave.setText("Update Task");

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Receive the taskId argument safely
        if (getArguments() != null) {
            taskId = getArguments().getInt("taskId", -1);
        }

        taskId = getArguments().getInt("taskId"); // retrieve the argument

        // Load the task details
        viewModel.getTaskById(taskId).observe(getViewLifecycleOwner(), task -> {
            if (task != null) {
                etTitle.setText(task.getTitle());
                etDescription.setText(task.getDescription());
            }
        });

        // Save updates
        btnSave.setOnClickListener(v -> {
            String newTitle = etTitle.getText().toString().trim();
            String newDesc = etDescription.getText().toString().trim();

            TaskEntity updated = new TaskEntity(newTitle, newDesc);
            updated.setId(taskId);

            viewModel.update(updated);

            Navigation.findNavController(view).navigateUp();
        });
    }
}
