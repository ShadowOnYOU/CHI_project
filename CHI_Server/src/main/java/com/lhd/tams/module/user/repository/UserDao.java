package com.lhd.tams.module.user.repository;

import com.lhd.tams.module.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByTeacherName(String teacherName); //通过用户名username查找用户，注意要按照JPA的格式使用驼峰命名法
    User findByTeacherNameAndPassword(String teacherName, String password);//通过用户名username和密码查找用户
}