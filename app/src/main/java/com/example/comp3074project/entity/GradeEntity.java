package com.example.comp3074project.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grades")
public class GradeEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String courseCode;
    private String courseName;
    private String assessmentName;
    private double grade;
    private double weight;
    private String feedback;

    // NEW: type of assessment (e.g., Exam, Quiz, Project)
    private String type;

    // ---------- Getters & Setters ----------
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getAssessmentName() { return assessmentName; }
    public void setAssessmentName(String assessmentName) { this.assessmentName = assessmentName; }

    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    // NEW
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
