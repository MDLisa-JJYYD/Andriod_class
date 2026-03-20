package com.example.studentmanager;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentmanager.adapters.CourseAdapter;
import com.example.studentmanager.db.DBHelper;
import com.example.studentmanager.models.Course;
import java.util.List;

public class SelectCourseActivity extends AppCompatActivity {

    private String studentId;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);

        studentId = getIntent().getStringExtra("studentId");
        dbHelper = new DBHelper(this);

        RecyclerView rv = findViewById(R.id.rv_available_courses);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Course> courses = dbHelper.getAllCourses();
        CourseAdapter adapter = new CourseAdapter(courses, course -> {
            long result = dbHelper.selectCourse(studentId, course.getId());
            if (result != -1) {
                Toast.makeText(this, "选课成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "该学生已选修此课程", Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);
    }
}
