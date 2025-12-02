package com.example.comp3074project.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.TaskEntity;
import com.example.comp3074project.viewModel.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TextView tvTotalTasks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --------------------- Floating Add Task Button ---------------------
        FloatingActionButton btnAddTask = view.findViewById(R.id.btn_add_new_task);
        btnAddTask.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_navigation_tasks_to_addTaskFragment)
        );

        // --------------------- RecyclerView setup ---------------------
        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(new ArrayList<>()); // initially empty
        recyclerView.setAdapter(adapter);

        // --------------------- Total tasks TextView ---------------------
        tvTotalTasks = view.findViewById(R.id.tvTotalTasks);

        // --------------------- ViewModel setup ---------------------
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            adapter.updateTasks(tasks); // update RecyclerView
            tvTotalTasks.setText(tasks.size() + " total tasks");
        });
    }
}
