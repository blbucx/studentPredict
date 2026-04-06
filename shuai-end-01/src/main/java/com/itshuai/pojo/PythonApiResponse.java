package com.itshuai.pojo;

import lombok.Data;

@Data
public class PythonApiResponse<T> {
    private Integer code;  // 状态码：200=成功，其他=失败
    private String msg;    // 提示信息
    private T data;        // 业务数据（单预测/批量预测的具体结果）
}