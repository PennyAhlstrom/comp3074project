package com.example.comp3074project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class AddReminderFragment extends Fragment {

    public AddReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cancel button navigates back to the previous screen
        Button cancelButton = view.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(v ->
                NavHostFragment.findNavController(AddReminderFragment.this).popBackStack()
        );

        // Save button will also just go back for now (no database yet)
        Button saveButton = view.findViewById(R.id.btn_save_reminder);
        saveButton.setOnClickListener(v ->
                NavHostFragment.findNavController(AddReminderFragment.this).popBackStack()
        );
    }
}
