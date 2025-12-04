package com.example.comp3074project.ui.tasks;

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

        FloatingActionButton btnAddTask = view.findViewById(R.id.btn_add_new_task);
        btnAddTask.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(R.id.addTaskFragment) // Navigate directly to AddTaskFragment
        );

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        tvTotalTasks = view.findViewById(R.id.tvTotalTasks);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            adapter.updateTasks(tasks);
            tvTotalTasks.setText(tasks.size() + " total tasks");
        });

        // "View All" button fix
        Button btnViewAllTasks = view.findViewById(R.id.btn_tasks_view_all);
        if (btnViewAllTasks != null) {
            btnViewAllTasks.setOnClickListener(v ->
                    Navigation.findNavController(view)
                            .navigate(R.id.navigation_tasks) // Navigate directly to TasksFragment
            );
        }
    }
}
