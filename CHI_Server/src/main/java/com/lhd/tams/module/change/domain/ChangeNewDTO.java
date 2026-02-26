package com.lhd.tams.module.change.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@ApiModel(value = "排课新增参数")
@Data
public class ChangeNewDTO {
    @ApiModelProperty
    private String course_name;
    @ApiModelProperty
    private String teacherName;
    @ApiModelProperty
    private Date old_date;
    @ApiModelProperty
    private Date new_date;
    @ApiModelProperty
    private Date old_date1;
    @ApiModelProperty
    private Date new_date1;
    @ApiModelProperty
    private Time old_start_time;
    @ApiModelProperty
    private Time old_end_time;
    @ApiModelProperty
    private Time new_start_time;
    @ApiModelProperty
    private Time new_end_time;
    @ApiModelProperty
    private String old_classroom_name;
    @ApiModelProperty
    private String new_classroom_name;
    @ApiModelProperty
    private String approval_status;
}
