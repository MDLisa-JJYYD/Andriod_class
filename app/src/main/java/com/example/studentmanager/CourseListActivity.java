package com.example.studentmanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentmanager.adapters.CourseAdapter;
import com.example.studentmanager.db.DBHelper;
import com.example.studentmanager.models.Course;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private RecyclerView rvCourses;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        dbHelper = new DBHelper(this);
        rvCourses = findViewById(R.id.rv_courses);
        rvCourses.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btn_add_course).setOnClickListener(v -> {
            startActivity(new Intent(this, AddCourseActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses();
    }

    private void loadCourses() {
        List<Course> courses = dbHelper.getAllCourses();
        CourseAdapter adapter = new CourseAdapter(courses, null); // 列表仅展示
        rvCourses.setAdapter(adapter);
    }
}
