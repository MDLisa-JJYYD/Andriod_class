package com.example.studentmanager.models;

import java.io.Serializable;

/**
 * 学生实体类
 */
public class Student implements Serializable {
    private String id;      // 学号
    private String name;    // 姓名
    private String gender;  // 性别
    private int age;        // 年龄
    private String phone;   // 电话

    public Student() {}

    public Student(String id, String name, String gender, int age, String phone) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
