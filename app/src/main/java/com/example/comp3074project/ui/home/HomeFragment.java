package com.example.comp3074project.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.comp3074project.R;

// This fragment shows the Home screen and handles navigation shortcuts
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get NavController from the activity's NavHostFragment
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        // ---------- REMINDERS ----------
        Button btnRemindersViewAll = view.findViewById(R.id.btn_reminders_view_all);
        btnRemindersViewAll.setOnClickListener(v ->
                navController.navigate(R.id.navigation_reminders)
        );

        Button btnRemindersAddNew = view.findViewById(R.id.btn_reminders_add_new);
        btnRemindersAddNew.setOnClickListener(v ->
                navController.navigate(R.id.addEditReminderFragment)
        );

        // ---------- TASKS ----------
        Button btnTasksViewAll = view.findViewById(R.id.btn_tasks_view_all);
        btnTasksViewAll.setOnClickListener(v ->
                navController.navigate(R.id.navigation_tasks)
        );

        Button btnTasksAddNew = view.findViewById(R.id.btn_tasks_add_new);
        btnTasksAddNew.setOnClickListener(v ->
                navController.navigate(R.id.addTaskFragment)
        );

        // ---------- COURSES ----------
        Button btnCoursesViewAll = view.findViewById(R.id.btn_courses_view_all);
        btnCoursesViewAll.setOnClickListener(v ->
                navController.navigate(R.id.navigation_courses)
        );

        Button btnCoursesAddNew = view.findViewById(R.id.btn_courses_add_new);
        btnCoursesAddNew.setOnClickListener(v ->
                navController.navigate(R.id.addCourseFragment)
        );

        // ---------- GRADES ----------
        Button btnGradesViewAll = view.findViewById(R.id.btn_grades_view_all);
        btnGradesViewAll.setOnClickListener(v ->
                navController.navigate(R.id.navigation_grades)
        );

        Button btnGradesAddNew = view.findViewById(R.id.btn_grades_add_new);
        btnGradesAddNew.setOnClickListener(v ->
                navController.navigate(R.id.addGradeFragment)
        );
    }

    public void scrollToTopOrRefresh() {
        // Example: scroll a RecyclerView to top if exists
        // RecyclerView recyclerView = getView().findViewById(R.id.recycler_home);
        // if (recyclerView != null) recyclerView.smoothScrollToPosition(0);

        // Or just refresh the content
        // loadHomeData(); // your method to refresh data

        Toast.makeText(getContext(), "Home refreshed!", Toast.LENGTH_SHORT).show();
    }

}
