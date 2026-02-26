package com.lhd.tams.module.cancle.controller;

import com.lhd.tams.common.base.BaseController;
import com.lhd.tams.common.model.ApiResult;
import com.lhd.tams.module.cancle.domain.data;
import com.lhd.tams.module.cancle.domain.dataNewDTO;
import com.lhd.tams.module.cancle.domain.dataVO;
import com.lhd.tams.module.cancle.service.CancleService;
import com.lhd.tams.module.change.domain.Change;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.lhd.tams.common.base.BaseController.successOrFail;

@RestController
@RequestMapping("/cancle")
@CrossOrigin(origins = "http://localhost:12011")
public class CancleController {
    @Resource
    private CancleService cancleService;
    @ApiOperation(value = "新增")
    @PostMapping("/saveData")
    public ResponseEntity<ApiResult<?>> saveData(@Validated @RequestBody dataNewDTO dataNewDTO) {
        return successOrFail(cancleService.newdata(dataNewDTO));
    }

    @ApiOperation(value = "删除")
    @PostMapping("/deleteData")
    public ResponseEntity<ApiResult<?>> deleteData(@Validated @RequestBody dataNewDTO dataNewDTO) {
        System.out.println("yes\n\n\n\n\n");
        return successOrFail(cancleService.deletedata(dataNewDTO));
    }

    @ApiOperation(value = "修改", response = data.class)
    @PostMapping("/getData")
    public ResponseEntity<ApiResult<List<dataVO>>> getData(@RequestBody dataNewDTO dataNewDTO) {
        return BaseController.success(cancleService.listChange(dataNewDTO));
    }

}