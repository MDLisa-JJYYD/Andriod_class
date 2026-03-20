package com.example.studentmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentmanager.db.DBHelper;
import com.example.studentmanager.models.Student;

public class AddStudentActivity extends AppCompatActivity {

    private EditText etId, etName, etAge, etPhone;
    private RadioGroup rgGender;
    private DBHelper dbHelper;
    private Student editStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        dbHelper = new DBHelper(this);
        etId = findViewById(R.id.et_stu_id);
        etName = findViewById(R.id.et_stu_name);
        etAge = findViewById(R.id.et_stu_age);
        etPhone = findViewById(R.id.et_stu_phone);
        rgGender = findViewById(R.id.rg_gender);
        Button btnSave = findViewById(R.id.btn_save_student);

        editStudent = (Student) getIntent().getSerializableExtra("student");
        if (editStudent != null) {
            etId.setText(editStudent.getId());
            etId.setEnabled(false); // 学号不可修改
            etName.setText(editStudent.getName());
            etAge.setText(String.valueOf(editStudent.getAge()));
            etPhone.setText(editStudent.getPhone());
            if ("女".equals(editStudent.getGender())) {
                ((RadioButton)findViewById(R.id.rb_female)).setChecked(true);
            }
            btnSave.setText("更新信息");
        }

        btnSave.setOnClickListener(v -> saveStudent());
    }

    private void saveStudent() {
        String id = etId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = rgGender.getCheckedRadioButtonId() == R.id.rb_male ? "男" : "女";

        if (id.isEmpty() || name.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        Student student = new Student(id, name, gender, age, phone);

        if (editStudent == null) {
            long result = dbHelper.addStudent(student);
            if (result != -1) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "学号已存在", Toast.LENGTH_SHORT).show();
            }
        } else {
            dbHelper.updateStudent(student);
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
