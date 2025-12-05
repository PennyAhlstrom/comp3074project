package com.example.comp3074project.ui.courses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.CourseEntity;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<CourseEntity> courses;
    private OnCourseClickListener listener;

    // Corrected constructor
    public CourseAdapter(List<CourseEntity> courses) {
        this.courses = courses;
    }

    // Setter for listener
    public void setOnCourseClickListener(OnCourseClickListener listener) {
        this.listener = listener;
    }

    // Listener interface
    public interface OnCourseClickListener {
        void onEdit(CourseEntity course);
        void onDelete(CourseEntity course);
    }

    public void updateCourses(List<CourseEntity> updatedCourses) {
        this.courses = updatedCourses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseEntity course = courses.get(position);

        holder.tvCourseName.setText(course.getName());
        holder.tvCourseCode.setText(course.getCode());
        holder.tvInstructor.setText("Instructor: " + course.getInstructor());
        holder.tvSchedule.setText("Schedule: " + course.getSchedule());

        // Attach edit/delete click events
        holder.btnEditCourse.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(course);
        });

        holder.btnDeleteCourse.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(course);
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseCode, tvInstructor, tvSchedule;
        ImageButton btnEditCourse, btnDeleteCourse;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);
            tvInstructor = itemView.findViewById(R.id.tvInstructor);
            tvSchedule = itemView.findViewById(R.id.tvSchedule);

            // These must exist in item_course.xml (tell me if you need the layout)
            btnEditCourse = itemView.findViewById(R.id.btn_edit_course);
            btnDeleteCourse = itemView.findViewById(R.id.btn_delete_course);
        }
    }
}
