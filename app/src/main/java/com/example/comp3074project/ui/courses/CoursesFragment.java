package com.example.comp3074project.ui.courses;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.CourseEntity;
import com.example.comp3074project.viewModel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CoursesFragment extends Fragment {

    private CourseViewModel courseViewModel;
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private TextView tvTotalCourses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Add course FAB
        FloatingActionButton btnAddCourse = view.findViewById(R.id.btn_add_new_course);
        btnAddCourse.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(R.id.addCourseFragment)
        );

        recyclerView = view.findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CourseAdapter(new ArrayList<>());

        recyclerView.setAdapter(adapter);

        tvTotalCourses = view.findViewById(R.id.tvTotalCourses);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        courseViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            adapter.updateCourses(courses);
            tvTotalCourses.setText(courses.size() + " enrolled courses");
        });

        // Add CRUD listeners
        adapter.setOnCourseClickListener(new CourseAdapter.OnCourseClickListener() {
            @Override
            public void onEdit(CourseEntity course) {
                Bundle bundle = new Bundle();
                bundle.putInt("courseId", course.getId());
                Navigation.findNavController(requireView())
                        .navigate(R.id.editCourseFragment, bundle);
            }

            @Override
            public void onDelete(CourseEntity course) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Course?")
                        .setMessage("Are you sure you want to delete this course?")
                        .setPositiveButton("Delete", (dialog, which) ->
                                courseViewModel.delete(course))
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        // View All fallback button
        Button btnViewAllCourses = view.findViewById(R.id.btn_courses_view_all);
        if (btnViewAllCourses != null) {
            btnViewAllCourses.setOnClickListener(v ->
                    Navigation.findNavController(view)
                            .navigate(R.id.navigation_courses)
            );
        }
    }
}
