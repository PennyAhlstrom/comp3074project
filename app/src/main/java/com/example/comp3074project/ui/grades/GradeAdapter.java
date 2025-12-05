package com.example.comp3074project.ui.grades;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.GradeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private List<GradeEntity> grades = new ArrayList<>();
    private OnGradeEditListener editListener;
    private OnGradeDeleteListener deleteListener;

    // Listener interfaces
    public interface OnGradeEditListener {
        void onEditGrade(GradeEntity grade);
    }

    public interface OnGradeDeleteListener {
        void onDeleteGrade(GradeEntity grade);
    }

    public GradeAdapter(OnGradeEditListener editListener, OnGradeDeleteListener deleteListener) {
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

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

        // Type and weight
        String typeText = grade.getType() != null ? grade.getType() : "Assessment";
        holder.tvTypeAndWeight.setText(String.format(Locale.getDefault(),
                "Type: %s \u2022 Worth %.1f%%", typeText, grade.getWeight()));

        // Feedback
        String feedback = grade.getFeedback();
        holder.tvFeedback.setText((feedback == null || feedback.trim().isEmpty()) ?
                "Feedback: (none)" : "Feedback: " + feedback);

        // Set edit button click
        holder.btnEditGrade.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEditGrade(grade);
            }
        });

        // Set delete button click with confirmation dialog
        holder.btnDeleteGrade.setOnClickListener(v -> {
            if (deleteListener != null) {
                showDeleteConfirmation(holder.itemView.getContext(), grade);
            }
        });
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    // ViewHolder class
    static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView tvAssessmentName;
        TextView tvGradeValue;
        TextView tvTypeAndWeight;
        TextView tvFeedback;
        ImageButton btnEditGrade;
        ImageButton btnDeleteGrade;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAssessmentName = itemView.findViewById(R.id.tvAssessmentName);
            tvGradeValue = itemView.findViewById(R.id.tvGradeValue);
            tvTypeAndWeight = itemView.findViewById(R.id.tvTypeAndWeight);
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
            btnEditGrade = itemView.findViewById(R.id.btn_edit_grade);
            btnDeleteGrade = itemView.findViewById(R.id.btn_delete_grade);
        }
    }

    // Show a confirmation dialog before deleting a grade
    private void showDeleteConfirmation(Context context, GradeEntity grade) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Grade")
                .setMessage("Are you sure you want to delete \"" + grade.getAssessmentName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (deleteListener != null) {
                        deleteListener.onDeleteGrade(grade);
                        Toast.makeText(context, "Grade deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
