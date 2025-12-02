package com.example.comp3074project.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comp3074project.db.AppDatabase;
import com.example.comp3074project.entity.CourseEntity;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final AppDatabase appDatabase;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
    }

    public LiveData<List<CourseEntity>> getAllCourses() {
        return appDatabase.courseDao().getAllCourses();
    }

    public void insert(CourseEntity course) {
        new Thread(() -> appDatabase.courseDao().insert(course)).start();
    }
}
