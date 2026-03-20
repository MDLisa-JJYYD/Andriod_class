package com.example.studentmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentmanager.adapters.ScoreAdapter;
import com.example.studentmanager.db.DBHelper;
import com.example.studentmanager.models.ScoreRecord;
import com.example.studentmanager.models.Student;
import java.util.List;

public class StudentDetailActivity extends AppCompatActivity {

    private Student student;
    private TextView tvInfo;
    private RecyclerView rvScores;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        dbHelper = new DBHelper(this);
        student = (Student) getIntent().getSerializableExtra("student");

        tvInfo = findViewById(R.id.tv_detail_info);
        rvScores = findViewById(R.id.rv_student_scores);
        rvScores.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

        findViewById(R.id.btn_edit_student).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentActivity.class);
            intent.putExtra("student", student);
            startActivity(intent);
        });

        findViewById(R.id.btn_delete_student).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("确认删除")
                    .setMessage("确定要删除该学生及其所有选课记录吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        dbHelper.deleteStudent(student.getId());
                        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

        findViewById(R.id.btn_select_course).setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectCourseActivity.class);
            intent.putExtra("studentId", student.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新加载学生信息（可能已修改）
        // 这里简单处理，实际应从数据库查最新
        loadScores();
    }

    private void updateUI() {
        String info = "学号：" + student.getId() + "\n" +
                     "姓名：" + student.getName() + "\n" +
                     "性别：" + student.getGender() + "\n" +
                     "年龄：" + student.getAge() + "\n" +
                     "电话：" + student.getPhone();
        tvInfo.setText(info);
    }

    private void loadScores() {
        List<ScoreRecord> scores = dbHelper.getStudentScores(student.getId());
        ScoreAdapter adapter = new ScoreAdapter(scores, record -> {
            // 点击修改成绩
            showEditScoreDialog(record);
        });
        rvScores.setAdapter(adapter);
    }

    private void showEditScoreDialog(ScoreRecord record) {
        final android.widget.EditText et = new android.widget.EditText(this);
        et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et.setText(String.valueOf(record.getScore()));
        
        new AlertDialog.Builder(this)
                .setTitle("录入/修改成绩")
                .setMessage("课程：" + record.getCourseName())
                .setView(et)
                .setPositiveButton("保存", (dialog, which) -> {
                    String scoreStr = et.getText().toString();
                    if (!scoreStr.isEmpty()) {
                        float score = Float.parseFloat(scoreStr);
                        dbHelper.updateScore(student.getId(), record.getCourseId(), score);
                        loadScores();
                        Toast.makeText(this, "成绩已更新", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
