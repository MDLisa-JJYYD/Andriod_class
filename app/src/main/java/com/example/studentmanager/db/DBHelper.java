package com.example.studentmanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studentmanager.models.Course;
import com.example.studentmanager.models.ScoreRecord;
import com.example.studentmanager.models.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具类，负责数据库的创建、升级及 CRUD 操作
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "StudentManagement.db";
    private static final int DB_VERSION = 1;

    // 表名
    public static final String TABLE_STUDENT = "Student";
    public static final String TABLE_COURSE = "Course";
    public static final String TABLE_SCORE = "Score";

    // Student 表字段
    public static final String COL_STU_ID = "id";
    public static final String COL_STU_NAME = "name";
    public static final String COL_STU_GENDER = "gender";
    public static final String COL_STU_AGE = "age";
    public static final String COL_STU_PHONE = "phone";

    // Course 表字段
    public static final String COL_COU_ID = "id";
    public static final String COL_COU_NAME = "name";
    public static final String COL_COU_CREDIT = "credit";

    // Score 表字段
    public static final String COL_SCO_STU_ID = "student_id";
    public static final String COL_SCO_COU_ID = "course_id";
    public static final String COL_SCO_GRADE = "score";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建学生表
        String createStudentTable = "CREATE TABLE " + TABLE_STUDENT + " (" +
                COL_STU_ID + " TEXT PRIMARY KEY, " +
                COL_STU_NAME + " TEXT, " +
                COL_STU_GENDER + " TEXT, " +
                COL_STU_AGE + " INTEGER, " +
                COL_STU_PHONE + " TEXT)";
        db.execSQL(createStudentTable);

        // 创建课程表
        String createCourseTable = "CREATE TABLE " + TABLE_COURSE + " (" +
                COL_COU_ID + " TEXT PRIMARY KEY, " +
                COL_COU_NAME + " TEXT, " +
                COL_COU_CREDIT + " INTEGER)";
        db.execSQL(createCourseTable);

        // 创建成绩表（关联表）
        String createScoreTable = "CREATE TABLE " + TABLE_SCORE + " (" +
                COL_SCO_STU_ID + " TEXT, " +
                COL_SCO_COU_ID + " TEXT, " +
                COL_SCO_GRADE + " REAL, " +
                "PRIMARY KEY (" + COL_SCO_STU_ID + ", " + COL_SCO_COU_ID + "), " +
                "FOREIGN KEY (" + COL_SCO_STU_ID + ") REFERENCES " + TABLE_STUDENT + "(" + COL_STU_ID + "), " +
                "FOREIGN KEY (" + COL_SCO_COU_ID + ") REFERENCES " + TABLE_COURSE + "(" + COL_COU_ID + "))";
        db.execSQL(createScoreTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }

    // --- 学生管理操作 ---

    public long addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STU_ID, student.getId());
        values.put(COL_STU_NAME, student.getName());
        values.put(COL_STU_GENDER, student.getGender());
        values.put(COL_STU_AGE, student.getAge());
        values.put(COL_STU_PHONE, student.getPhone());
        return db.insert(TABLE_STUDENT, null, values);
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENT, null);
        if (cursor.moveToFirst()) {
            do {
                Student s = new Student(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                );
                list.add(s);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STU_NAME, student.getName());
        values.put(COL_STU_GENDER, student.getGender());
        values.put(COL_STU_AGE, student.getAge());
        values.put(COL_STU_PHONE, student.getPhone());
        return db.update(TABLE_STUDENT, values, COL_STU_ID + "=?", new String[]{student.getId()});
    }

    public void deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 先删除该学生的选课记录
        db.delete(TABLE_SCORE, COL_SCO_STU_ID + "=?", new String[]{id});
        // 再删除学生信息
        db.delete(TABLE_STUDENT, COL_STU_ID + "=?", new String[]{id});
    }

    // --- 课程管理操作 ---

    public long addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_COU_ID, course.getId());
        values.put(COL_COU_NAME, course.getName());
        values.put(COL_COU_CREDIT, course.getCredit());
        return db.insert(TABLE_COURSE, null, values);
    }

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSE, null);
        if (cursor.moveToFirst()) {
            do {
                Course c = new Course(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2)
                );
                list.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- 选课与成绩管理 ---

    /**
     * 为学生分配课程（选课）
     */
    public long selectCourse(String studentId, String courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SCO_STU_ID, studentId);
        values.put(COL_SCO_COU_ID, courseId);
        values.put(COL_SCO_GRADE, 0.0f); // 初始成绩为0
        return db.insert(TABLE_SCORE, null, values);
    }

    /**
     * 修改成绩
     */
    public int updateScore(String studentId, String courseId, float score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SCO_GRADE, score);
        return db.update(TABLE_SCORE, values, COL_SCO_STU_ID + "=? AND " + COL_SCO_COU_ID + "=?",
                new String[]{studentId, courseId});
    }

    /**
     * 联表查询：查询某个学生的所有选课记录及成绩
     * 逻辑解释：
     * 通过 Score 表作为桥梁，连接 Student 表和 Course 表。
     * 使用 JOIN 语句将 Course 表的信息（课程名、学分）与 Score 表中的成绩信息合并。
     */
    public List<ScoreRecord> getStudentScores(String studentId) {
        List<ScoreRecord> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT s." + COL_SCO_COU_ID + ", c." + COL_COU_NAME + ", c." + COL_COU_CREDIT + ", s." + COL_SCO_GRADE +
                " FROM " + TABLE_SCORE + " s " +
                " JOIN " + TABLE_COURSE + " c ON s." + COL_SCO_COU_ID + " = c." + COL_COU_ID +
                " WHERE s." + COL_SCO_STU_ID + " = ?";
        
        Cursor cursor = db.rawQuery(sql, new String[]{studentId});
        if (cursor.moveToFirst()) {
            do {
                ScoreRecord record = new ScoreRecord();
                record.setStudentId(studentId);
                record.setCourseId(cursor.getString(0));
                record.setCourseName(cursor.getString(1));
                record.setCredit(cursor.getInt(2));
                record.setScore(cursor.getFloat(3));
                list.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
