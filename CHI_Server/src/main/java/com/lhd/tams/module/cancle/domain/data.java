package com.lhd.tams.module.cancle.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "t_cancle")
public class data {
    @Getter
    @Setter
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    @Column(name = "course_name")
    private String course_name;
    @Getter
    @Setter
    @Column(name = "teacher_name")
    private String teacherName;
    @Getter
    @Setter
    @Column(name = "date")
    private Date date;
    @Getter
    @Setter
    @Column(name = "attend_time")
    private Time attend_time;
    @Getter
    @Setter
    @Column(name = "finish_time")
    private Time finish_time;
    @Getter
    @Setter
    @Column(name = "classroom_name")
    private String classroom_name;

}