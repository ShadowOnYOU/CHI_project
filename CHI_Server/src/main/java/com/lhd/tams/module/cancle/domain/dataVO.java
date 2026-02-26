package com.lhd.tams.module.cancle.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@ApiModel()
@Data
public class dataVO {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty
    private String course_name;
    @ApiModelProperty
    private String teacher_name;
    @ApiModelProperty
    private Date date;
    @ApiModelProperty
    private Time attend_time;
    @ApiModelProperty
    private Time finish_time;
    @ApiModelProperty
    private String classroom_name;

}