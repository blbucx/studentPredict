package com.itshuai.pojo;

import lombok.Data;

@Data
public class StudentPredictRes {
    private String name;            // 学生姓名
    private Double predictScore;    // 相对预测分数
    private String investmentLevel; // 投入性等级
    private String explanation;     // 投入性解释
    private String grade;           // 成绩等级
}