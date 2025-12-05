package com.example.comp3074project.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.comp3074project.db.AppDatabase;
import com.example.comp3074project.entity.GradeEntity;

public class GradeViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final ExecutorService executorService;

    public GradeViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<GradeEntity>> getAllGrades() {
        return db.gradeDao().getAllGrades();
    }

    public LiveData<List<GradeEntity>> getGradesByCourse(String courseCode) {
        return db.gradeDao().getGradesByCourse(courseCode);
    }

    // NEW: fetch a single grade by ID
    public LiveData<GradeEntity> getGradeById(int gradeId) {
        return db.gradeDao().getGradeById(gradeId);
    }

    public void insert(GradeEntity grade) {
        executorService.execute(() -> db.gradeDao().insert(grade));
    }

    public void update(GradeEntity grade) {
        executorService.execute(() -> db.gradeDao().update(grade));
    }

    public void delete(GradeEntity grade) {
        executorService.execute(() -> db.gradeDao().delete(grade));
    }
}
