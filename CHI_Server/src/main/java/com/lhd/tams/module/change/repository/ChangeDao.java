package com.lhd.tams.module.change.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lhd.tams.module.change.domain.Change;
import com.lhd.tams.module.coursescheduling.model.vo.CourseSchedulingListVO;
import com.lhd.tams.module.student.domain.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeDao extends JpaRepository<Change, Long> {
    List<Change> findAll();
    List<Change> findByTeacherName(String teacherName);
}
