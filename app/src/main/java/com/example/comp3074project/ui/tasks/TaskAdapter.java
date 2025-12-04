package com.example.comp3074project.ui.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3074project.R;
import com.example.comp3074project.entity.TaskEntity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskEntity> tasks;

    private OnTaskClickListener listener;

    public TaskAdapter(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskEntity task = tasks.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());

        // Subtle background highlight for completed tasks
        if (task.isCompleted()) {
            holder.cardTask.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.light_green));
        } else {
            holder.cardTask.setCardBackgroundColor(holder.itemView.getResources().getColor(android.R.color.white));
        }

        // Edit/Delete button click listeners
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(task);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(task);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // Called when LiveData updates
    public void updateTasks(List<TaskEntity> newTasks) {
        tasks = newTasks;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        ImageButton btnEdit, btnDelete;
        MaterialCardView cardTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTask = itemView.findViewById(R.id.cardTask);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvDescription = itemView.findViewById(R.id.tvTaskDescription);
            btnEdit = itemView.findViewById(R.id.btn_edit_task);
            btnDelete = itemView.findViewById(R.id.btn_delete_task);

            // Set click listeners
            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEdit(tasks.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDelete(tasks.get(position));
                }
            });
        }
    }

    public interface OnTaskClickListener {
        void onEdit(TaskEntity task);
        void onDelete(TaskEntity task);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }
}
