package com.example.comp3074project.ui.grades;

import android.os.Bundle;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.GradeEntity;
import com.example.comp3074project.viewModel.GradeViewModel;

public class EditGradeFragment extends Fragment {

    private GradeViewModel gradeViewModel;
    private int gradeId;

    private EditText etCourseCode, etCourseName, etAssessmentName, etGrade, etWeight, etFeedback;
    private Spinner spinnerGradeType;
    private Button btnUpdateGrade, btnDeleteGrade;

    private GradeEntity currentGrade; // reference to loaded grade

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_grade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind views
        etCourseCode = view.findViewById(R.id.inputCourseCode);
        etCourseName = view.findViewById(R.id.inputCourseName);
        etAssessmentName = view.findViewById(R.id.inputAssessmentName);
        spinnerGradeType = view.findViewById(R.id.spinnerGradeType);
        etGrade = view.findViewById(R.id.inputGrade);
        etWeight = view.findViewById(R.id.inputWeight);
        etFeedback = view.findViewById(R.id.inputFeedback);
        btnUpdateGrade = view.findViewById(R.id.btnUpdateGrade);
        btnDeleteGrade = view.findViewById(R.id.btnDeleteGrade);

        gradeViewModel = new ViewModelProvider(this).get(GradeViewModel.class);

        // --- Setup spinner adapter safely ---
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.grade_types, // ensure this exists in res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGradeType.setAdapter(adapter);

        // Get gradeId from arguments safely
        gradeId = getArguments() != null ? getArguments().getInt("gradeId", -1) : -1;
        if (gradeId == -1) {
            Toast.makeText(getContext(), "Invalid grade ID", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_editGradeFragment_to_navigation_grades);
            return;
        }

        // Load existing grade safely
        gradeViewModel.getGradeById(gradeId).observe(getViewLifecycleOwner(), grade -> {
            if (grade != null) {
                currentGrade = grade; // keep reference for deletion

                etCourseCode.setText(grade.getCourseCode());
                etCourseName.setText(grade.getCourseName());
                etAssessmentName.setText(grade.getAssessmentName());

                // Set spinner selection safely
                String type = grade.getType();
                if (type != null) {
                    for (int i = 0; i < spinnerGradeType.getCount(); i++) {
                        if (spinnerGradeType.getItemAtPosition(i).toString().equals(type)) {
                            spinnerGradeType.setSelection(i);
                            break;
                        }
                    }
                }

                etGrade.setText(String.valueOf(grade.getGrade()));
                etWeight.setText(String.valueOf(grade.getWeight()));
                etFeedback.setText(grade.getFeedback());
            } else {
                Toast.makeText(getContext(), "Grade not found", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_editGradeFragment_to_navigation_grades);
            }
        });

        // Button click listeners
        btnUpdateGrade.setOnClickListener(v -> updateGrade());
        btnDeleteGrade.setOnClickListener(v -> deleteGrade());
    }

    // --- Update grade safely ---
    private void updateGrade() {
        String courseCode = etCourseCode.getText().toString().trim();
        String courseName = etCourseName.getText().toString().trim();
        String assessmentName = etAssessmentName.getText().toString().trim();
        String gradeStr = etGrade.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String feedback = etFeedback.getText().toString().trim();
        String type = spinnerGradeType.getSelectedItem().toString();

        if (courseCode.isEmpty() || courseName.isEmpty() || assessmentName.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (gradeStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(getContext(), "Grade and Weight must be numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        double gradeValue;
        double weight;
        try {
            gradeValue = Double.parseDouble(gradeStr);
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        GradeEntity updatedGrade = new GradeEntity(courseCode, courseName, assessmentName, type, gradeValue, weight, feedback);
        updatedGrade.setId(gradeId);

        gradeViewModel.update(updatedGrade);
        Toast.makeText(getContext(), "Grade updated", Toast.LENGTH_SHORT).show();

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_editGradeFragment_to_navigation_grades);
    }

    // --- Delete grade safely ---
    private void deleteGrade() {
        if (currentGrade == null) {
            Toast.makeText(getContext(), "Cannot delete: grade not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        gradeViewModel.delete(currentGrade);
        Toast.makeText(getContext(), "Grade deleted", Toast.LENGTH_SHORT).show();

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_editGradeFragment_to_navigation_grades);
    }
}
