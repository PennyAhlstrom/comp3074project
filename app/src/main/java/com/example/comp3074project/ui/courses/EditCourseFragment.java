package com.example.comp3074project.ui.courses;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.CourseEntity;
import com.example.comp3074project.viewModel.CourseViewModel;

import java.util.Calendar;

public class EditCourseFragment extends Fragment {

    private CourseViewModel courseViewModel;
    private int courseId;

    private EditText etCourseName;
    private EditText etCourseCode;
    private EditText etInstructor;
    private EditText etFromDate;
    private EditText etToDate;
    private EditText etLectureTime;
    private EditText etLabTime;

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

        etCourseName = view.findViewById(R.id.etCourseName);
        etCourseCode = view.findViewById(R.id.etCourseCode);
        etInstructor = view.findViewById(R.id.etInstructor);
        etFromDate = view.findViewById(R.id.etScheduleDate);
        etToDate = view.findViewById(R.id.etScheduleDate2);
        etLectureTime = view.findViewById(R.id.LectureTime);
        etLabTime = view.findViewById(R.id.LabTime);

        btnUpdateCourse = view.findViewById(R.id.btnUpdateCourse);
        btnDeleteCourse = view.findViewById(R.id.btnDeleteCourse);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        if (getArguments() != null) {
            courseId = getArguments().getInt("courseId");
        }

        // Load course data
        courseViewModel.getCourseById(courseId).observe(getViewLifecycleOwner(), course -> {
            if (course != null) {
                etCourseName.setText(course.getName());
                etCourseCode.setText(course.getCode());
                etInstructor.setText(course.getInstructor());
                etFromDate.setText(course.getFromDate());
                etToDate.setText(course.getToDate());
                etLectureTime.setText(course.getLectureTime());
                etLabTime.setText(course.getLabTime());
            }
        });

        // Date pickers
        etFromDate.setOnClickListener(v -> showDatePicker(etFromDate));
        etToDate.setOnClickListener(v -> showDatePicker(etToDate));

        // Time pickers
        etLectureTime.setOnClickListener(v -> showTimePicker(etLectureTime));
        etLabTime.setOnClickListener(v -> showTimePicker(etLabTime));

        btnUpdateCourse.setOnClickListener(v -> updateCourse(view));
        btnDeleteCourse.setOnClickListener(v -> deleteCourse(view));
    }

    private void updateCourse(View view) {
        String name = etCourseName.getText().toString().trim();
        String code = etCourseCode.getText().toString().trim();
        String instructor = etInstructor.getText().toString().trim();
        String fromDate = etFromDate.getText().toString().trim();
        String toDate = etToDate.getText().toString().trim();
        String lectureTime = etLectureTime.getText().toString().trim();
        String labTime = etLabTime.getText().toString().trim();

        if (name.isEmpty() || code.isEmpty() || instructor.isEmpty() ||
                fromDate.isEmpty() || toDate.isEmpty() ||
                lectureTime.isEmpty() || labTime.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        CourseEntity updatedCourse = new CourseEntity(name, code, instructor, fromDate, toDate, lectureTime, labTime);
        updatedCourse.setId(courseId);

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
                etFromDate.getText().toString(),
                etToDate.getText().toString(),
                etLectureTime.getText().toString(),
                etLabTime.getText().toString()
        );
        toDelete.setId(courseId);

        courseViewModel.delete(toDelete);

        Toast.makeText(getContext(), "Course deleted", Toast.LENGTH_SHORT).show();

        Navigation.findNavController(view)
                .navigate(R.id.action_editCourseFragment_to_navigation_courses);
    }

    private void showDatePicker(EditText targetField) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                (DatePicker dp, int y, int m, int d) -> targetField.setText(String.format("%04d-%02d-%02d", y, m + 1, d)),
                year, month, day
        );

        dialog.show();
    }

    private void showTimePicker(EditText targetField) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                getContext(),
                (TimePicker tp, int h, int m) -> targetField.setText(String.format("%02d:%02d", h, m)),
                hour, minute, true
        );

        dialog.show();
    }
}
