package com.example.comp3074project.ui.grades;

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
import com.example.comp3074project.entity.GradeEntity;
import com.example.comp3074project.viewModel.GradeViewModel;

import java.util.List;
import java.util.Locale;

public class GradesFragment extends Fragment {

    private GradeViewModel gradeViewModel;
    private GradeAdapter gradeAdapter;

    private TextView tvGradesSubtitle;
    private TextView tvCourseCode;
    private TextView tvCourseName;
    private TextView tvAverageValue;
    private TextView tvWeightedTotal;
    private RecyclerView recyclerGrades;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvGradesSubtitle = view.findViewById(R.id.tvGradesSubtitle);
        tvCourseCode = view.findViewById(R.id.tvCourseCode);
        tvCourseName = view.findViewById(R.id.tvCourseName);
        tvAverageValue = view.findViewById(R.id.tvAverageValue);
        tvWeightedTotal = view.findViewById(R.id.tvWeightedTotal);
        recyclerGrades = view.findViewById(R.id.recyclerGrades);

        recyclerGrades.setLayoutManager(new LinearLayoutManager(getContext()));
        gradeAdapter = new GradeAdapter();
        recyclerGrades.setAdapter(gradeAdapter);

        gradeViewModel = new ViewModelProvider(this).get(GradeViewModel.class);
        gradeViewModel.getAllGrades().observe(getViewLifecycleOwner(), grades -> {
            updateSummary(grades);
            gradeAdapter.updateGrades(grades);
        });

        // "View All" button fix (if exists on this fragment)
        Button btnViewAllGrades = view.findViewById(R.id.btn_grades_view_all);
        if (btnViewAllGrades != null) {
            btnViewAllGrades.setOnClickListener(v ->
                    Navigation.findNavController(view)
                            .navigate(R.id.navigation_grades)
            );
        }
    }

    private void updateSummary(List<GradeEntity> grades) {
        int count = grades != null ? grades.size() : 0;
        tvGradesSubtitle.setText(count + " recorded grade" + (count != 1 ? "s" : ""));

        if (count == 0) {
            tvCourseCode.setText("No course selected");
            tvCourseName.setText("Add a grade to see details");
            tvAverageValue.setText("0%");
            tvWeightedTotal.setText("Based on 0% of final grade");
            return;
        }

        GradeEntity first = grades.get(0);
        tvCourseCode.setText(first.getCourseCode());
        tvCourseName.setText(first.getCourseName());

        double totalWeightedScore = 0.0;
        double totalWeight = 0.0;

        for (GradeEntity g : grades) {
            totalWeightedScore += g.getGrade() * g.getWeight();
            totalWeight += g.getWeight();
        }

        if (totalWeight > 0) {
            double weightedAverage = totalWeightedScore / totalWeight;
            tvAverageValue.setText(String.format(Locale.getDefault(), "%.1f%%", weightedAverage));
            tvWeightedTotal.setText(String.format(Locale.getDefault(),
                    "Based on %.1f%% of final grade", totalWeight));
        } else {
            tvAverageValue.setText("0%");
            tvWeightedTotal.setText("Based on 0% of final grade");
        }
    }
}
