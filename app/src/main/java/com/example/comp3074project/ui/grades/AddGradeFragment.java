package com.example.comp3074project.ui.grades;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp3074project.databinding.FragmentAddGradeBinding;
import com.example.comp3074project.entity.CourseEntity;
import com.example.comp3074project.entity.GradeEntity;
import com.example.comp3074project.viewModel.CourseViewModel;
import com.example.comp3074project.viewModel.GradeViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddGradeFragment extends Fragment {

    private FragmentAddGradeBinding binding;
    private CourseViewModel courseViewModel;
    private GradeViewModel gradeViewModel;

    private final List<CourseEntity> courseList = new ArrayList<>();
    private boolean isSyncing = false;      // prevents infinite callback loop
    private int selectedCourseIndex = -1;

    private final String[] gradeTypes = {
            "Class Work", "Lab", "Assignment",
            "Midterm Exam", "Final Exam", "Quiz", "Project", "Other"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddGradeBinding.inflate(inflater, container, false);

        gradeViewModel  = new ViewModelProvider(this).get(GradeViewModel.class);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        setupCourseSpinners();
        setupTypeSpinner();
        setupSaveButton();

        binding.btnDeleteGrade.setVisibility(View.GONE);   // Not used in Add mode

        return binding.getRoot();
    }

    private void setupCourseSpinners() {
        courseViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {

            courseList.clear();
            courseList.addAll(courses);

            if (courseList.isEmpty()) {
                Toast.makeText(requireContext(), "Please add a course first.", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> names = new ArrayList<>();
            List<String> codes = new ArrayList<>();

            for (CourseEntity c : courseList) {
                names.add(c.getName());
                codes.add(c.getCode());
            }

            ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    names
            );
            nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ArrayAdapter<String> codeAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    codes
            );
            codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            binding.spinnerCourse.setAdapter(nameAdapter);
            binding.spinnerCourseCode.setAdapter(codeAdapter);

            selectedCourseIndex = 0;   // preload first course

            isSyncing = true;
            binding.spinnerCourse.setSelection(0);
            binding.spinnerCourseCode.setSelection(0);
            isSyncing = false;

            binding.spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                    if (isSyncing) return;
                    selectedCourseIndex = pos;

                    isSyncing = true;
                    binding.spinnerCourseCode.setSelection(pos);
                    isSyncing = false;
                }

                @Override public void onNothingSelected(AdapterView<?> p) {}
            });

            binding.spinnerCourseCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                    if (isSyncing) return;
                    selectedCourseIndex = pos;

                    isSyncing = true;
                    binding.spinnerCourse.setSelection(pos);
                    isSyncing = false;
                }

                @Override public void onNothingSelected(AdapterView<?> p) {}
            });
        });
    }

    private void setupTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                gradeTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerGradeType.setAdapter(adapter);
        binding.spinnerGradeType.setSelection(0);
    }

    private void setupSaveButton() {
        binding.btnSaveGrade.setOnClickListener(v -> {

            if (selectedCourseIndex < 0) {
                Toast.makeText(requireContext(), "Please select a course.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEmpty(binding.inputGrade) || isEmpty(binding.inputWeight)) {
                Toast.makeText(requireContext(), "Grade and weight are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            double gradeValue;
            double weightValue;

            try {
                gradeValue  = Double.parseDouble(binding.inputGrade.getText().toString().trim());
                weightValue = Double.parseDouble(binding.inputWeight.getText().toString().trim());
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Invalid grade or weight value.", Toast.LENGTH_SHORT).show();
                return;
            }

            CourseEntity course = courseList.get(selectedCourseIndex);

            GradeEntity grade = new GradeEntity();
            grade.setCourseName(course.getName());
            grade.setCourseCode(course.getCode());
            grade.setType(binding.spinnerGradeType.getSelectedItem().toString());
            grade.setGrade(gradeValue);
            grade.setWeight(weightValue);
            grade.setFeedback(binding.inputFeedback.getText().toString().trim());

            gradeViewModel.insert(grade);
            Toast.makeText(requireContext(), "Grade saved.", Toast.LENGTH_SHORT).show();

            clearForm();
        });
    }

    private boolean isEmpty(View view) {
        return TextUtils.isEmpty(((android.widget.TextView) view).getText());
    }

    private void clearForm() {
        binding.inputGrade.setText("");
        binding.inputWeight.setText("");
        binding.inputFeedback.setText("");
        binding.spinnerGradeType.setSelection(0);

        if (!courseList.isEmpty()) {
            isSyncing = true;
            binding.spinnerCourse.setSelection(0);
            binding.spinnerCourseCode.setSelection(0);
            isSyncing = false;
            selectedCourseIndex = 0;
        }
    }
}
