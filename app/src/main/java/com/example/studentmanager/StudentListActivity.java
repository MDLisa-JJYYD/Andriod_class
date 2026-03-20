package com.example.studentmanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentmanager.adapters.StudentAdapter;
import com.example.studentmanager.db.DBHelper;
import com.example.studentmanager.models.Student;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private RecyclerView rvStudents;
    private StudentAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        dbHelper = new DBHelper(this);
        rvStudents = findViewById(R.id.rv_students);
        rvStudents.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btn_add_student).setOnClickListener(v -> {
            startActivity(new Intent(StudentListActivity.this, AddStudentActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    private void loadStudents() {
        List<Student> students = dbHelper.getAllStudents();
        adapter = new StudentAdapter(students, student -> {
            Intent intent = new Intent(StudentListActivity.this, StudentDetailActivity.class);
            intent.putExtra("student", student);
            startActivity(intent);
        });
        rvStudents.setAdapter(adapter);
    }
}
