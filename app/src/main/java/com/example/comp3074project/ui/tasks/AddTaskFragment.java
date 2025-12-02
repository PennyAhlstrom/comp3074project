package com.example.comp3074project.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.TaskEntity;
import com.example.comp3074project.viewModel.TaskViewModel;

public class AddTaskFragment extends Fragment {

    private TaskViewModel taskViewModel;

    private EditText etTaskTitle;
    private EditText etTaskDescription;
    private Button btnSaveTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTaskTitle = view.findViewById(R.id.etTaskTitle);
        etTaskDescription = view.findViewById(R.id.etTaskDescription);
        btnSaveTask = view.findViewById(R.id.btnSaveTask);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        btnSaveTask.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = etTaskTitle.getText().toString().trim();
        String description = etTaskDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Task title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskEntity task = new TaskEntity(title, description, false);
        taskViewModel.insert(task);

        Toast.makeText(getContext(), "Task saved", Toast.LENGTH_SHORT).show();

        // Optional: navigate back or clear form
        etTaskTitle.setText("");
        etTaskDescription.setText("");
    }
}
