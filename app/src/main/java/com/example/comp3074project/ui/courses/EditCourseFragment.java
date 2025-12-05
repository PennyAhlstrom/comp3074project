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

public class EditCourseFragment extends Fragment {

    private CourseViewModel courseViewModel;
    private int courseId;

    private EditText etCourseName;
    private EditText etCourseCode;
    private EditText etInstructor;
    private EditText etSchedule;
    private Button btnUpdateCourse;
    private Button btnDeleteCourse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind views
        etCourseName = view.findViewById(R.id.etCourseName);
        etCourseCode = view.findViewById(R.id.etCourseCode);
        etInstructor = view.findViewById(R.id.etInstructor);
        etSchedule = view.findViewById(R.id.etSchedule);
        btnUpdateCourse = view.findViewById(R.id.btnUpdateCourse);
        btnDeleteCourse = view.findViewById(R.id.btnDeleteCourse);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Get courseId manually from arguments
        if (getArguments() != null) {
            courseId = getArguments().getInt("courseId");
        }

        // Load course data
        courseViewModel.getCourseById(courseId).observe(getViewLifecycleOwner(), course -> {
            if (course != null) {
                etCourseName.setText(course.getName());
                etCourseCode.setText(course.getCode());
                etInstructor.setText(course.getInstructor());
                etSchedule.setText(course.getSchedule());
            }
        });

        // Handle Update Button
        btnUpdateCourse.setOnClickListener(v -> updateCourse(view));

        // Handle Delete Button
        btnDeleteCourse.setOnClickListener(v -> deleteCourse(view));
    }

    private void updateCourse(View view) {
        String name = etCourseName.getText().toString().trim();
        String code = etCourseCode.getText().toString().trim();
        String instructor = etInstructor.getText().toString().trim();
        String schedule = etSchedule.getText().toString().trim();

        if (name.isEmpty() || code.isEmpty() || instructor.isEmpty() || schedule.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        CourseEntity updatedCourse = new CourseEntity(name, code, instructor, schedule);
        updatedCourse.setId(courseId); // IMPORTANT: keep same ID

        courseViewModel.update(updatedCourse);

        Toast.makeText(getContext(), "Course updated", Toast.LENGTH_SHORT).show();

        Navigation.findNavController(view)
                .navigate(R.id.action_editCourseFragment_to_navigation_courses);
    }

    private void deleteCourse(View view) {
        CourseEntity toDelete = new CourseEntity(
                etCourseName.getText().toString(),
                etCourseCode.getText().toString(),
                etInstructor.getText().toString(),
                etSchedule.getText().toString()
        );
        toDelete.setId(courseId);

        courseViewModel.delete(toDelete);

        Toast.makeText(getContext(), "Course deleted", Toast.LENGTH_SHORT).show();

        Navigation.findNavController(view)
                .navigate(R.id.action_editCourseFragment_to_navigation_courses);
    }
}
