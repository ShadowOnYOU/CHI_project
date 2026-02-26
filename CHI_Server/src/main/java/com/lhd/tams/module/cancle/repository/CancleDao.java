package com.lhd.tams.module.cancle.repository;

import com.lhd.tams.module.cancle.domain.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancleDao extends JpaRepository<data, Long> {
    List<data> findAll();

    List<data> findByTeacherName(String teacherName);
}
