package com.freecharge.dashboard.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "backend_automation")
@Data
public class DashBoardResult {

    @Id
    @Column(name="test_class_name")
    private String testClassName;

    @Column(name="test_method_name")
    private String testMethodName;

    @Column(name="start_time")
    private String startTime;

    @Column(name="desc")
    private String desc;

    @Column(name="status")
    private String status;



}
