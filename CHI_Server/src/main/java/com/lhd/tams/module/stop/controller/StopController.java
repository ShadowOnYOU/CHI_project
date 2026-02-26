package com.lhd.tams.module.stop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lhd.tams.common.base.BaseController;
import com.lhd.tams.common.model.ApiResult;
import com.lhd.tams.module.classroom.model.data.ClassroomDO;
import com.lhd.tams.module.classroom.service.ClassroomService;
import com.lhd.tams.module.coursescheduling.manager.CourseSchedulingExcelManager;
import com.lhd.tams.module.coursescheduling.model.data.CourseSchedulingDO;
import com.lhd.tams.module.coursescheduling.model.dto.*;
import com.lhd.tams.module.coursescheduling.model.vo.CourseSchedulingListVO;
import com.lhd.tams.module.coursescheduling.service.CourseSchedulingService;
import com.lhd.tams.module.user.domain.User;
import com.lhd.tams.module.user.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("stop-course-scheduling-list")
@RestController
@CrossOrigin(origins = "http://localhost:12011")
public class StopController extends BaseController {

    @Autowired
    private CourseSchedulingService courseSchedulingService;

    @Autowired
    private ClassroomService classroomService;

    @PostMapping("/id")
    public ResponseEntity<ApiResult<Long>> getCourseId(@RequestParam String classroomName, @RequestParam String date, @RequestParam String attendTime){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = LocalDate.parse(date, formatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


        LocalTime localTime = LocalTime.parse(attendTime, timeFormatter);

        log.debug("attend" + attendTime);

        Long classroomId = classroomService.getClassroomIdByName(classroomName);


        return success(courseSchedulingService.getCourseSchedulingIdByFactors(classroomId, localDate, localTime));

    }


}