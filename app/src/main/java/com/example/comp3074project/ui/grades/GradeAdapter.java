package com.example.comp3074project.ui.grades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.GradeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private List<GradeEntity> grades = new ArrayList<>();

    public GradeAdapter() {}

    /**
     * Update the grades list dynamically and refresh the RecyclerView.
     */
    public void updateGrades(List<GradeEntity> newGrades) {
        this.grades = newGrades;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        GradeEntity grade = grades.get(position);

        // Set assessment name and grade value
        holder.tvAssessmentName.setText(grade.getAssessmentName());
        holder.tvGradeValue.setText(String.format(Locale.getDefault(), "%.1f%%", grade.getGrade()));

        // Use the 'type' from GradeEntity if available, otherwise default to "Assessment"
        String typeText = grade.getType() != null ? grade.getType() : "Assessment";
        holder.tvTypeAndWeight.setText(
                String.format(Locale.getDefault(),
                        "Type: %s \u2022 Worth %.1f%%",
                        typeText,
                        grade.getWeight())
        );

        // Display feedback
        String feedback = grade.getFeedback();
        if (feedback == null || feedback.trim().isEmpty()) {
            holder.tvFeedback.setText("Feedback: (none)");
        } else {
            holder.tvFeedback.setText("Feedback: " + feedback);
        }
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    /**
     * ViewHolder class for RecyclerView items.
     */
    static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView tvAssessmentName;
        TextView tvGradeValue;
        TextView tvTypeAndWeight;
        TextView tvFeedback;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAssessmentName = itemView.findViewById(R.id.tvAssessmentName);
            tvGradeValue = itemView.findViewById(R.id.tvGradeValue);
            tvTypeAndWeight = itemView.findViewById(R.id.tvTypeAndWeight);
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
        }
    }
}
