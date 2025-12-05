package com.example.comp3074project.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comp3074project.db.AppDatabase;
import com.example.comp3074project.dao.CourseDao;
import com.example.comp3074project.entity.CourseEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseViewModel extends AndroidViewModel {

    private final CourseDao courseDao;
    private final LiveData<List<CourseEntity>> allCourses;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CourseViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        courseDao = db.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public LiveData<List<CourseEntity>> getAllCourses() {
        return allCourses;
    }

    public LiveData<CourseEntity> getCourseById(int id) {
        return courseDao.getCourseById(id);
    }

    public void insert(CourseEntity course) {
        executor.execute(() -> courseDao.insert(course));
    }

    public void update(CourseEntity course) {
        executor.execute(() -> courseDao.update(course));
    }

    public void delete(CourseEntity course) {
        executor.execute(() -> courseDao.delete(course));
    }
}
