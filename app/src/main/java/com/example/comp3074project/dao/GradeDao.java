package com.example.comp3074project.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.comp3074project.entity.GradeEntity;

@Dao
public interface GradeDao {

    @Insert
    void insert(GradeEntity grade);

    @Update
    void update(GradeEntity grade);

    @Delete
    void delete(GradeEntity grade);

    @Query("SELECT * FROM grades ORDER BY courseCode ASC")
    LiveData<List<GradeEntity>> getAllGrades();

    @Query("SELECT * FROM grades WHERE courseCode = :courseCode")
    LiveData<List<GradeEntity>> getGradesByCourse(String courseCode);
}
