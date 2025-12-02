package com.example.comp3074project.ui.courses;

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
import androidx.navigation.Navigation;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.CourseEntity;
import com.example.comp3074project.viewModel.CourseViewModel;

public class AddCourseFragment extends Fragment {

    private CourseViewModel courseViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etCourseName = view.findViewById(R.id.etCourseName);
        EditText etCourseCode = view.findViewById(R.id.etCourseCode);
        EditText etInstructor = view.findViewById(R.id.etInstructor);
        EditText etSchedule = view.findViewById(R.id.etSchedule);
        Button btnSaveCourse = view.findViewById(R.id.btnSaveCourse);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        btnSaveCourse.setOnClickListener(v -> {
            String name = etCourseName.getText().toString().trim();
            String code = etCourseCode.getText().toString().trim();
            String instructor = etInstructor.getText().toString().trim();
            String schedule = etSchedule.getText().toString().trim();

            if (name.isEmpty() || code.isEmpty() || instructor.isEmpty() || schedule.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            CourseEntity course = new CourseEntity(name, code, instructor, schedule);
            courseViewModel.insert(course);

            Toast.makeText(getContext(), "Course added", Toast.LENGTH_SHORT).show();

            Navigation.findNavController(view).navigate(R.id.action_addCourseFragment_to_navigation_courses);
        });
    }
}
