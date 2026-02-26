package com.lhd.tams.module.classroom.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lhd.tams.common.base.BaseController;
import com.lhd.tams.common.model.ApiResult;
import com.lhd.tams.module.classroom.model.dto.ClassroomPageQuery;
import com.lhd.tams.module.classroom.model.dto.ClassroomSaveDTO;
import com.lhd.tams.module.classroom.model.vo.ClassroomListVO;
import com.lhd.tams.module.classroom.service.ClassroomService;
import com.lhd.tams.module.student.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "教室")
@RequestMapping("classroom")
@RestController
public class ClassroomController extends BaseController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentService studentService;

    @ApiOperation(value = "分页列表", response = ClassroomListVO.class)
    @GetMapping
    public ResponseEntity<ApiResult<IPage<ClassroomListVO>>> pageCourse(ClassroomPageQuery pageQuery) {

        return success(classroomService.pageCourse(pageQuery));
    }

    @ApiOperation(value = "参照列表", response = ClassroomListVO.class)
    @GetMapping("list/ref")
    public ResponseEntity<ApiResult<List<ClassroomListVO>>> refList() {

        return success(classroomService.refList());
    }

    @ApiOperation(value = "详情", response = ClassroomListVO.class)
    @GetMapping("{id}")
    public ResponseEntity<ApiResult<List<ClassroomListVO>>> getCourseById(@PathVariable("id") Long id) {
        List<ClassroomListVO> classroomListVOList = classroomService.refList();
        System.out.println(classroomListVOList.size());
        int studentNum = studentService.getStudentNum(id);
        System.out.println(studentNum);
        List<ClassroomListVO> result = new ArrayList<>();
        for (ClassroomListVO classroomListVO : classroomListVOList){
            if(classroomListVO.getSize() > studentNum){
                result.add(classroomListVO);
            }
        }
        return success(result);
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public ResponseEntity<ApiResult<?>> saveCourse(@Validated @RequestBody ClassroomSaveDTO saveDTO) {

        return successOrFail(classroomService.saveCourse(saveDTO));
    }

    @ApiOperation(value = "修改")
    @PutMapping("{id}")
    public ResponseEntity<ApiResult<?>> updateCourseById(@PathVariable("id") Long id, @Validated @RequestBody ClassroomSaveDTO saveDTO) {

        return successOrFail(classroomService.updateCourseById(id, saveDTO));
    }

    @ApiOperation(value = "停启用")
    @PutMapping("{id}/enable-state/{enableState}")
    public ResponseEntity<ApiResult<?>> updateCourseEnableStateById(@PathVariable("id") Long id, @PathVariable("enableState") Integer enableState) {

        return successOrFail(classroomService.updateCourseEnableStateById(id, enableState));
    }
}
