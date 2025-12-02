package com.example.comp3074project.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.comp3074project.entity.ReminderEntity;
import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    void insert(ReminderEntity reminder);

    @Query("SELECT * FROM reminders ORDER BY alertDate, alertTime")
    LiveData<List<ReminderEntity>> getAllReminders();
}
