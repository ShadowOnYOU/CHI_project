package com.lhd.tams.module.change.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@ApiModel(value = "调课列表返回结果")
@Data
public class ChangeVO {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty
    private String course_name;
    @ApiModelProperty
    private String teacher_name;
    @ApiModelProperty
    private Date old_date;
    @ApiModelProperty
    private Date new_date;
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
