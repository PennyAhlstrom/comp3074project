package com.example.comp3074project.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.comp3074project.entity.CourseEntity;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    void insert(CourseEntity course);

    @Update
    void update(CourseEntity course);

    @Delete
    void delete(CourseEntity course);

    @Query("SELECT * FROM courses ORDER BY id DESC")
    LiveData<List<CourseEntity>> getAllCourses();

    @Query("SELECT * FROM courses WHERE id = :id LIMIT 1")
    LiveData<CourseEntity> getCourseById(int id);
}
