package com.example.comp3074project.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class ReminderEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String title;

    public String associatedTask;
    public String course;
    public String priority;
    public String alertDate;
    public String alertTime;
    public String note;

    public ReminderEntity(@NonNull String title, String associatedTask, String course,
                          String priority, String alertDate, String alertTime, String note) {
        this.title = title;
        this.associatedTask = associatedTask;
        this.course = course;
        this.priority = priority;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
        this.note = note;
    }
}