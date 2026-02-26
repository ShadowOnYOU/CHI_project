package com.lhd.tams.module.student.reposittory;

import com.lhd.tams.module.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao extends JpaRepository<Student, Long> {
    List<Student> findByStudentId(String studentId);
    List<Student> findByCourseScheduleId(Long courseScheduleId);
}
