package com.example.comp3074project.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comp3074project.dao.ReminderDao;
import com.example.comp3074project.db.AppDatabase;
import com.example.comp3074project.entity.ReminderEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderViewModel extends AndroidViewModel {

    private final ReminderDao reminderDao;
    private final LiveData<List<ReminderEntity>> allReminders;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        reminderDao = db.reminderDao();
        allReminders = reminderDao.getAllReminders();
    }

    public LiveData<List<ReminderEntity>> getAllReminders() {
        return allReminders;
    }

    public void insert(ReminderEntity reminder) {
        executor.execute(() -> reminderDao.insert(reminder));
    }

    public void update(ReminderEntity reminder) {
        executor.execute(() -> reminderDao.update(reminder));
    }

    public void delete(ReminderEntity reminder) {
        executor.execute(() -> reminderDao.delete(reminder));
    }
}
