package com.lhd.tams.module.change.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Table(name = "t_course_change")
@Entity
public class Change {
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
    @Column(name = "old_date")
    private Date old_date;
    @Getter
    @Setter
    @Column(name = "new_date")
    private Date new_date;
    @Getter
    @Setter
    @Column(name = "old_start_time")
    private Time old_start_time;
    @Getter
    @Setter
    @Column(name = "old_end_time")
    private Time old_end_time;
    @Getter
    @Setter
    @Column(name = "new_start_time")
    private Time new_start_time;
    @Getter
    @Setter
    @Column(name = "new_end_time")
    private Time new_end_time;
    @Getter
    @Setter
    @Column(name = "old_classroom_name")
    private String old_classroom_name;
    @Getter
    @Setter
    @Column(name = "new_classroom_name")
    private String new_classroom_name;
    @Getter
    @Setter
    @Column(name = "approval_status")
    private String approval_status;
}
