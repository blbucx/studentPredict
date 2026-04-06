package com.itshuai.pojo;

import lombok.Data;

@Data  // Lombok自动生成get/set/toString等方法
public class StudentPredictReq {
    private String name;          // 学生姓名
    private Double audio;         // 课程音视频(100%)
    private Double learningTime;  // 章节学习次数(100%)
    private Double assignment;    // 作业(100%)
    private Double signTime;      // 签到(100%)
    private Double courseInteract;// 课程互动(100%)
}