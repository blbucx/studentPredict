package com.itshuai.controller;


import com.itshuai.pojo.BatchPredictRes;
import com.itshuai.pojo.StudentPredictReq;
import com.itshuai.pojo.StudentPredictRes;
import com.itshuai.service.PredictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@RestController
public class PredictController {
    @Autowired
    private PredictService predictService;


    // 单学生预测接口（接收JSON参数）
    @PostMapping("/api/student/predict")
    public StudentPredictRes singlePredict(@RequestBody StudentPredictReq req) {
        // 调用服务类，返回Python的预测结果
        log.info("接收参数：{}", req);
        return predictService.callSinglePredict(req);
    }


    // 批量Excel预测接口（接收文件上传）
    @PostMapping("/api/student/batch-predict")
    public BatchPredictRes batchPredict(@RequestParam("file") MultipartFile excelFile) {
        // 调用服务类，返回Python的批量预测结果
        log.info("接收文件参数：{}",excelFile);
        return predictService.callBatchPredict(excelFile);
    }
}