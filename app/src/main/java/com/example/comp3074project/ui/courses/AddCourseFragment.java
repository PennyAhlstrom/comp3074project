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
        EditText etScheduleDate = view.findViewById(R.id.etScheduleDate);
        EditText etScheduleDate2 = view.findViewById(R.id.etScheduleDate2);
        EditText etLectureTime = view.findViewById(R.id.LectureTime);
        EditText etLabTime = view.findViewById(R.id.LabTime);

        Button btnSaveCourse = view.findViewById(R.id.btnSaveCourse);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // Date pickers
        etScheduleDate.setOnClickListener(v -> showDatePicker(etScheduleDate));
        etScheduleDate2.setOnClickListener(v -> showDatePicker(etScheduleDate2));

        // Time pickers
        etLectureTime.setOnClickListener(v -> showTimePicker(etLectureTime));
        etLabTime.setOnClickListener(v -> showTimePicker(etLabTime));

        btnSaveCourse.setOnClickListener(v -> {
            String name = etCourseName.getText().toString().trim();
            String code = etCourseCode.getText().toString().trim();
            String instructor = etInstructor.getText().toString().trim();
            String fromDate = etScheduleDate.getText().toString().trim();
            String toDate = etScheduleDate2.getText().toString().trim();
            String lectureTime = etLectureTime.getText().toString().trim();
            String labTime = etLabTime.getText().toString().trim();

            if (name.isEmpty() || code.isEmpty() || instructor.isEmpty() ||
                    fromDate.isEmpty() || toDate.isEmpty() ||
                    lectureTime.isEmpty() || labTime.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            CourseEntity course = new CourseEntity(name, code, instructor, fromDate, toDate, lectureTime, labTime);
            courseViewModel.insert(course);

            Toast.makeText(getContext(), "Course added", Toast.LENGTH_SHORT).show();

            Navigation.findNavController(view)
                    .navigate(R.id.action_addCourseFragment_to_navigation_courses);
        });
    }

    private void showDatePicker(EditText targetField) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (DatePicker view1, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String date = String.format("%04d-%02d-%02d",
                            selectedYear, selectedMonth + 1, selectedDay);
                    targetField.setText(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showTimePicker(EditText targetField) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (TimePicker tp, int h, int m) -> {
                    String time = String.format("%02d:%02d", h, m);
                    targetField.setText(time);
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }
}
