package com.lhd.tams.module.student.service.Impl;

import com.lhd.tams.module.student.domain.Student;
import com.lhd.tams.module.student.reposittory.StudentDao;
import com.lhd.tams.module.student.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentDao studentDao;

    @Override
    public Long getConflictStudentNumber(Long courseScheduleId, List<Long> conflictCourseScheduleIdList) {
        List<Student> studentList = studentDao.findByCourseScheduleId(courseScheduleId);
        List<String> studentIdList = new ArrayList<>();
        for (Student item : studentList) {
            studentIdList.add(item.getStudentId());
        }

        List<Student> conflictStudentList = studentDao.findByCourseScheduleId(conflictCourseScheduleIdList.get(0));
        for(int i = 1; i < conflictCourseScheduleIdList.size(); i++){
            conflictStudentList.addAll(studentDao.findByCourseScheduleId(conflictCourseScheduleIdList.get(i)));
        }
        Set<String> conflictStudentIdSet = new HashSet<>();
        for(int i = 0; i < conflictStudentList.size(); i++){
            conflictStudentIdSet.add(conflictStudentList.get(i).getStudentId());
        }
        List<String> conflictStudentIdList = new ArrayList<>(conflictStudentIdSet);

        Long num = studentIdList.stream()
                .filter(conflictStudentIdList::contains)
                .count();
        return num;
    }

    @Override
    public List<Long> getCourseScheduleIdList(Long courseScheduleId) {
        List<Student> studentList = studentDao.findByCourseScheduleId(courseScheduleId);
        String studentId = studentList.get(0).getStudentId();
        List<Student> selectedStudentList = studentDao.findByStudentId(studentId);
        Set<Long> courseScheduleIdList = new HashSet<>();
        for (Student student : selectedStudentList) {
            courseScheduleIdList.add(student.getCourseScheduleId());
        }
        if(studentList.size() == 20){
            String studentIdMore = studentList.get(19).getStudentId();
            List<Student> selectedStudentListMore = studentDao.findByStudentId(studentIdMore);
            for (Student student : selectedStudentListMore) {
                courseScheduleIdList.add(student.getCourseScheduleId());
            }
        }
        return new ArrayList<>(courseScheduleIdList);
    }

    @Override
    public int getStudentNum(Long courseScheduleId){
        List<Student> studentList = studentDao.findByCourseScheduleId(courseScheduleId);
        return studentList.size();
    }
}
