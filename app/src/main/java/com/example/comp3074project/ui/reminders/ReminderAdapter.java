package com.example.comp3074project.ui.reminders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.ReminderEntity;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private List<ReminderEntity> reminders = new ArrayList<>();

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ReminderEntity reminder = reminders.get(position);
        holder.tvTitle.setText(reminder.title);
        holder.tvTask.setText(reminder.associatedTask);
        holder.tvCourse.setText(reminder.course);
        holder.tvPriority.setText(reminder.priority);
        holder.tvDateTime.setText(reminder.alertDate + " " + reminder.alertTime);
        holder.tvNote.setText(reminder.note != null ? reminder.note : "");
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void setReminders(List<ReminderEntity> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTask, tvCourse, tvPriority, tvDateTime, tvNote;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTask = itemView.findViewById(R.id.tvTask);
            tvCourse = itemView.findViewById(R.id.tvCourse);
            tvPriority = itemView.findViewById(R.id.tvPriority);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvNote = itemView.findViewById(R.id.tvNote);
        }
    }
}
