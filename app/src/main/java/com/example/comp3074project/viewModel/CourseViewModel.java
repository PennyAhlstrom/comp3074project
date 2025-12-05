package com.example.comp3074project.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comp3074project.db.AppDatabase;
import com.example.comp3074project.entity.CourseEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel for CourseEntity.
 * Exposes LiveData for observing courses and handles asynchronous CRUD operations.
 */
public class CourseViewModel extends AndroidViewModel {

    private final AppDatabase db;
    private final ExecutorService executorService;
    private final LiveData<List<CourseEntity>> allCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
        executorService = Executors.newSingleThreadExecutor();
        allCourses = db.courseDao().getAllCourses();
    }

    // ---------- LiveData Methods ----------

    /**
     * Returns a LiveData list of all courses.
     */
    public LiveData<List<CourseEntity>> getAllCourses() {
        return allCourses;
    }

    /**
     * Fetch a single CourseEntity by its ID.
     */
    public LiveData<CourseEntity> getCourseById(int courseId) {
        return db.courseDao().getCourseById(courseId);
    }

    // ---------- CRUD Operations ----------

    /**
     * Insert a new course asynchronously.
     */
    public void insert(CourseEntity course) {
        if (course.getName() == null || course.getCode() == null) {
            throw new IllegalArgumentException("Course name and code must not be null");
        }
        executorService.execute(() -> db.courseDao().insert(course));
    }

    /**
     * Update an existing course asynchronously.
     */
    public void update(CourseEntity course) {
        executorService.execute(() -> db.courseDao().update(course));
    }

    /**
     * Delete a course asynchronously.
     */
    public void delete(CourseEntity course) {
        executorService.execute(() -> db.courseDao().delete(course));
    }
}
