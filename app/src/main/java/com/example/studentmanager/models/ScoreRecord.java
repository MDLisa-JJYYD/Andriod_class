package com.example.studentmanager.models;

/**
 * 选课成绩记录实体类（用于展示联表查询结果）
 */
public class ScoreRecord {
    private String studentId;
    private String courseId;
    private String courseName;
    private int credit;
    private float score;

    public ScoreRecord() {}

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }

    public float getScore() { return score; }
    public void setScore(float score) { this.score = score; }
}
