package com.example.comp3074project.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.ReminderEntity;
import com.example.comp3074project.viewModel.ReminderViewModel;

import java.util.List;

public class RemindersFragment extends Fragment {

    private ReminderViewModel reminderViewModel;
    private ReminderAdapter adapter;

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewReminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ReminderAdapter();
        recyclerView.setAdapter(adapter);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Observe LiveData from ViewModel
        reminderViewModel.getAllReminders().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(List<ReminderEntity> reminders) {
        adapter.setReminders(reminders);
    }
}
