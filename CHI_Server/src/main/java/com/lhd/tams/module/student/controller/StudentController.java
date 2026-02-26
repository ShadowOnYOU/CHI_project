package com.lhd.tams.module.student.controller;

import com.lhd.tams.module.student.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:12011")
public class StudentController {
    @Resource
    private StudentService studentService;

}
