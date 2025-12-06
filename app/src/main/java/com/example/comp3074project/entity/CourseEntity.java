package com.example.comp3074project.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String code;
    private String instructor;

    // Existing date fields
    private String fromDate;
    private String toDate;

    // NEW: time fields
    private String lectureTime;
    private String labTime;

    // Updated constructor
    public CourseEntity(String name, String code, String instructor, String fromDate, String toDate,
                        String lectureTime, String labTime) {
        this.name = name;
        this.code = code;
        this.instructor = instructor;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.lectureTime = lectureTime;
        this.labTime = labTime;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }

    public String getLectureTime() { return lectureTime; }
    public void setLectureTime(String lectureTime) { this.lectureTime = lectureTime; }

    public String getLabTime() { return labTime; }
    public void setLabTime(String labTime) { this.labTime = labTime; }
}
