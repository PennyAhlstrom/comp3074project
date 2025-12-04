package com.example.comp3074project.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "reminders")
public class ReminderEntity implements Serializable {

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

    private Integer taskId;
    private Integer courseId;

    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public ReminderEntity(@NonNull String title, String associatedTask, String course,
                          String priority, String alertDate, String alertTime, String note, Integer taskId, Integer courseId) {
        this.title = title;
        this.associatedTask = associatedTask;
        this.course = course;
        this.priority = priority;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
        this.note = note;
        this.courseId = courseId;
        this.taskId = taskId;
    }
}