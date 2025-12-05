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
import androidx.navigation.Navigation;

import com.example.comp3074project.databinding.FragmentAddGradeBinding;
import com.example.comp3074project.entity.CourseEntity;
import com.example.comp3074project.entity.GradeEntity;
import com.example.comp3074project.viewModel.CourseViewModel;
import com.example.comp3074project.viewModel.GradeViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditGradeFragment extends Fragment {

    private FragmentAddGradeBinding binding;
    private GradeViewModel gradeViewModel;
    private CourseViewModel courseViewModel;

    private GradeEntity grade;
    private final List<CourseEntity> courseList = new ArrayList<>();
    private boolean isSyncing = false;
    private int selectedCourseIndex = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddGradeBinding.inflate(inflater, container, false);

        gradeViewModel  = new ViewModelProvider(this).get(GradeViewModel.class);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // 🔁 Use plain Bundle instead of EditGradeFragmentArgs
        int gradeId = -1;
        if (getArguments() != null) {
            gradeId = getArguments().getInt("gradeId", -1);
        }

        if (gradeId == -1) {
            Toast.makeText(requireContext(), "No gradeId passed to EditGradeFragment.", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).popBackStack();
            return binding.getRoot();
        }

        gradeViewModel.getGradeById(gradeId).observe(getViewLifecycleOwner(), loaded -> {
            if (loaded != null) {
                grade = loaded;
                preloadFields();
                setupCourseSpinners();
                setupSaveButton();
                setupDeleteButton();
            } else {
                Toast.makeText(requireContext(), "Grade not found.", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).popBackStack();
            }
        });

        return binding.getRoot();
    }

    private void preloadFields() {
        binding.inputGrade.setText(String.valueOf(grade.getGrade()));
        binding.inputWeight.setText(String.valueOf(grade.getWeight()));
        binding.inputFeedback.setText(grade.getFeedback());
    }

    private void setupCourseSpinners() {
        courseViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {

            courseList.clear();
            courseList.addAll(courses);

            if (courseList.isEmpty()) {
                Toast.makeText(requireContext(), "No courses available.", Toast.LENGTH_SHORT).show();
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

            ArrayAdapter<String> codeAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    codes
            );

            nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            binding.spinnerCourse.setAdapter(nameAdapter);
            binding.spinnerCourseCode.setAdapter(codeAdapter);

            // Preselect correct course based on grade’s stored data
            selectedCourseIndex = findMatchingCourseIndex(grade.getCourseCode(), grade.getCourseName());

            if (selectedCourseIndex < 0) selectedCourseIndex = 0;

            isSyncing = true;
            binding.spinnerCourse.setSelection(selectedCourseIndex);
            binding.spinnerCourseCode.setSelection(selectedCourseIndex);
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

    private int findMatchingCourseIndex(String code, String name) {
        for (int i = 0; i < courseList.size(); i++) {
            CourseEntity c = courseList.get(i);
            if (c.getCode().equals(code) || c.getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private void setupSaveButton() {
        binding.btnSaveGrade.setOnClickListener(v -> {

            if (selectedCourseIndex < 0) {
                Toast.makeText(requireContext(), "Please select a course.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(binding.inputGrade.getText())
                    || TextUtils.isEmpty(binding.inputWeight.getText())) {

                Toast.makeText(requireContext(), "Grade and Weight are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            double gradeValue = Double.parseDouble(binding.inputGrade.getText().toString());
            double weightValue = Double.parseDouble(binding.inputWeight.getText().toString());

            CourseEntity selected = courseList.get(selectedCourseIndex);

            grade.setCourseCode(selected.getCode());
            grade.setCourseName(selected.getName());
            grade.setGrade(gradeValue);
            grade.setWeight(weightValue);
            grade.setFeedback(binding.inputFeedback.getText().toString());

            gradeViewModel.update(grade);
            Toast.makeText(requireContext(), "Grade updated.", Toast.LENGTH_SHORT).show();

            Navigation.findNavController(requireView()).popBackStack();
        });
    }

    private void setupDeleteButton() {
        binding.btnDeleteGrade.setOnClickListener(v -> {
            gradeViewModel.delete(grade);
            Toast.makeText(requireContext(), "Grade deleted.", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).popBackStack();
        });
    }
}
