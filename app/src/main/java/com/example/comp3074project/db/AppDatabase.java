package com.example.comp3074project.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.comp3074project.dao.ReminderDao;
import com.example.comp3074project.dao.TaskDao;
import com.example.comp3074project.entity.ReminderEntity;
import com.example.comp3074project.entity.TaskEntity;

@Database(entities = {ReminderEntity.class, TaskEntity.class}, version = 2) // increment version
public abstract class AppDatabase extends RoomDatabase {

    public abstract ReminderDao reminderDao();
    public abstract TaskDao taskDao();  // <-- added TaskDao

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration() // handle version updates
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
