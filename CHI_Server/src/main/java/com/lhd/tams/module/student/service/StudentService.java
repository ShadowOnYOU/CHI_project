package com.lhd.tams.module.student.service;

import java.util.List;

public interface StudentService {
    Long getConflictStudentNumber(Long courseScheduleId, List<Long> conflictCourseScheduleIdList);
    List<Long> getCourseScheduleIdList(Long courseScheduleId);

    int getStudentNum(Long courseScheduleId);
}
