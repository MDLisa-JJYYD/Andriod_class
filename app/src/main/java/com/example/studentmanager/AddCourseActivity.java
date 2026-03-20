package com.example.studentmanager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentmanager.db.DBHelper;
import com.example.studentmanager.models.Course;

public class AddCourseActivity extends AppCompatActivity {

    private EditText etId, etName, etCredit;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        dbHelper = new DBHelper(this);
        etId = findViewById(R.id.et_course_id);
        etName = findViewById(R.id.et_course_name);
        etCredit = findViewById(R.id.et_course_credit);

        findViewById(R.id.btn_save_course).setOnClickListener(v -> {
            String id = etId.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String creditStr = etCredit.getText().toString().trim();

            if (id.isEmpty() || name.isEmpty() || creditStr.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            Course course = new Course(id, name, Integer.parseInt(creditStr));
            long result = dbHelper.addCourse(course);
            if (result != -1) {
                Toast.makeText(this, "课程录入成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "课程号已存在", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
