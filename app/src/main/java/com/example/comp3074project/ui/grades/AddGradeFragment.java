package com.example.comp3074project.ui.grades;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comp3074project.databinding.FragmentAddGradeBinding;
import com.example.comp3074project.entity.GradeEntity;
import com.example.comp3074project.viewModel.GradeViewModel;

public class AddGradeFragment extends Fragment {

    private FragmentAddGradeBinding binding;
    private GradeViewModel viewModel;

    // Predefined assessment types
    private final String[] gradeTypes = {"Assessment Type: ", "Class Work", "Lab", "Assignment", "Midterm Exam", "Final Exam", "Quiz", "Project", "Other"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddGradeBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(GradeViewModel.class);

        setupTypeSpinner();
        setupSaveButton();

        return binding.getRoot();
    }

    private void setupTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                gradeTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGradeType.setAdapter(adapter);
    }

    private void setupSaveButton() {
        binding.btnSaveGrade.setOnClickListener(v -> {
            // Validate required fields
            if (TextUtils.isEmpty(binding.inputCourseCode.getText()) ||
                    TextUtils.isEmpty(binding.inputCourseName.getText()) ||
                    TextUtils.isEmpty(binding.inputGrade.getText()) ||
                    TextUtils.isEmpty(binding.inputWeight.getText())) {

                Toast.makeText(requireContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create and populate GradeEntity
            GradeEntity grade = new GradeEntity();
            grade.setCourseCode(binding.inputCourseCode.getText().toString().trim());
            grade.setCourseName(binding.inputCourseName.getText().toString().trim());
            grade.setGrade(Double.parseDouble(binding.inputGrade.getText().toString().trim()));
            grade.setWeight(Double.parseDouble(binding.inputWeight.getText().toString().trim()));
            grade.setFeedback(binding.inputFeedback.getText().toString().trim());

            // Set type from Spinner
            grade.setType(binding.spinnerGradeType.getSelectedItem().toString());

            // Insert into database
            viewModel.insert(grade);

            // Show confirmation Toast
            Toast.makeText(requireContext(), "Grade saved successfully!", Toast.LENGTH_SHORT).show();

            // Clear input fields for next entry
            binding.inputCourseCode.setText("");
            binding.inputCourseName.setText("");
            binding.inputGrade.setText("");
            binding.inputWeight.setText("");
            binding.inputFeedback.setText("");

            // Reset spinner to first option
            binding.spinnerGradeType.setSelection(0);

            // Focus back to first input
            binding.inputCourseCode.requestFocus();
        });
    }
}
