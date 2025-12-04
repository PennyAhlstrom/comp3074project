package com.example.comp3074project.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comp3074project.db.AppDatabase;
import com.example.comp3074project.dao.TaskDao;
import com.example.comp3074project.entity.TaskEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskViewModel extends AndroidViewModel {

    private final TaskDao taskDao;
    private final LiveData<List<TaskEntity>> allTasks;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TaskViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<TaskEntity>> getAllTasks() {
        return allTasks;
    }

    public void insert(TaskEntity task) {
        executor.execute(() -> taskDao.insert(task));
    }

    public void update(TaskEntity task) {
        executor.execute(() -> taskDao.update(task));
    }

    public void delete(TaskEntity task) {
        executor.execute(() -> taskDao.delete(task));
    }

    public LiveData<TaskEntity> getTaskById(int id) {
        return taskDao.getTaskById(id);
    }
}
