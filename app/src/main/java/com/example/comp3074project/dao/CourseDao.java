package com.example.comp3074project.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.comp3074project.entity.CourseEntity;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    void insert(CourseEntity course);

    @Query("SELECT * FROM courses ORDER BY id DESC")
    LiveData<List<CourseEntity>> getAllCourses();
}
