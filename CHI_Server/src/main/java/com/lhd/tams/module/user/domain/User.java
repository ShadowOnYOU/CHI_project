package com.lhd.tams.module.user.domain;

import javax.persistence.*;

@Table(name = "teacher_table")
@Entity
public class User {

    @Id
    @Column(name = "teacher_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long teacherId;

    @Column(name = "teacher_name")
    private String teacherName;

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "password")
    // 密码属性varchar对应String
    private String password;

}