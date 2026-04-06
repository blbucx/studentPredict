package com.itshuai.pojo;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class BatchPredictRes {
    private Integer total;          // 总预测条数
    private Map<String, Map<String, Integer>> statistics; // 统计信息（investment/grade）
    private List<Map<String, Object>> detail; // 每条学生的预测详情
}
