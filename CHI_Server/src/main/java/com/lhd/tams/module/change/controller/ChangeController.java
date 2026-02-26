package com.lhd.tams.module.change.controller;

import com.lhd.tams.common.model.ApiResult;
import com.lhd.tams.module.change.domain.Change;
import com.lhd.tams.module.change.domain.ChangeNewDTO;
import com.lhd.tams.module.change.domain.ChangeVO;
import com.lhd.tams.module.change.service.ChangeService;
import com.lhd.tams.module.coursescheduling.model.dto.CourseSchedulingQuery;
import com.lhd.tams.module.coursescheduling.model.dto.CourseSchedulingSaveDTO;
import com.lhd.tams.module.coursescheduling.model.vo.CourseSchedulingListVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.lhd.tams.common.base.BaseController;
import javax.annotation.Resource;
import java.util.List;

import static com.lhd.tams.common.base.BaseController.successOrFail;


@RestController
@RequestMapping("/change")
@CrossOrigin(origins = "http://localhost:12011")
public class ChangeController {
    @Resource
    private ChangeService changeService;
    @ApiOperation(value = "修改", response = Change.class)
    @PostMapping("/list")
    public ResponseEntity<ApiResult<List<ChangeVO>>> listChange(@RequestBody ChangeNewDTO changeNewDTO) {
        return BaseController.success(changeService.listChange(changeNewDTO));
    }
    @ApiOperation(value = "新增")
    @PostMapping("/new")
    public ResponseEntity<ApiResult<?>> newChange(@Validated @RequestBody ChangeNewDTO changeNewDTO) {
        return successOrFail(changeService.newChange(changeNewDTO));
    }
}
