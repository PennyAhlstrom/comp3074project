package com.example.comp3074project.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comp3074project.db.AppDatabase;
import com.example.comp3074project.entity.GradeEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel for GradeEntity.
 * Handles CRUD operations asynchronously using ExecutorService.
 * Exposes LiveData for UI observation.
 */
public class GradeViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final ExecutorService executorService;

    public GradeViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    // ---------- LiveData Methods ----------

    /**
     * Returns all grades in the database.
     */
    public LiveData<List<GradeEntity>> getAllGrades() {
        return db.gradeDao().getAllGrades();
    }

    /**
     * Returns all grades for a specific courseCode.
     */
    public LiveData<List<GradeEntity>> getGradesByCourse(String courseCode) {
        return db.gradeDao().getGradesByCourse(courseCode);
    }

    /**
     * Fetch a single GradeEntity by its ID.
     */
    public LiveData<GradeEntity> getGradeById(int gradeId) {
        return db.gradeDao().getGradeById(gradeId);
    }

    // ---------- CRUD Operations ----------

    /**
     * Insert a new grade asynchronously.
     * Ensures courseCode and courseName are already populated in GradeEntity.
     */
    public void insert(GradeEntity grade) {
        if (grade.getCourseCode() == null || grade.getCourseName() == null) {
            throw new IllegalArgumentException("Course Code and Course Name must not be null");
        }
        executorService.execute(() -> db.gradeDao().insert(grade));
    }

    /**
     * Update an existing grade asynchronously.
     */
    public void update(GradeEntity grade) {
        executorService.execute(() -> db.gradeDao().update(grade));
    }

    /**
     * Delete a grade asynchronously.
     */
    public void delete(GradeEntity grade) {
        executorService.execute(() -> db.gradeDao().delete(grade));
    }
}
