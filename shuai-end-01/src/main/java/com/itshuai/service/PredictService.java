package com.itshuai.service;

//import com.example.pythonapiclient.entity.*;
import com.itshuai.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PredictService {
    @Autowired
    private RestTemplate restTemplate;

    // ********** 配置Python API地址（必须与Python服务一致）**********
    private static final String PYTHON_BASE_URL = "http://127.0.0.1:8189";
    private static final String SINGLE_PREDICT_URL = PYTHON_BASE_URL + "/api/predict/single";
    private static final String BATCH_PREDICT_URL = PYTHON_BASE_URL + "/api/predict/batch";


    // 1. 调用Python单学生预测API
    public StudentPredictRes callSinglePredict(StudentPredictReq req) {
        try {
            // ① 设置请求头：指定为JSON格式（Python端需接收JSON）
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<StudentPredictReq> requestEntity = new HttpEntity<>(req, headers);

            // ② 发送POST请求，调用Python API
            ResponseEntity<PythonApiResponse<StudentPredictRes>> response = restTemplate.exchange(
                    SINGLE_PREDICT_URL,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<PythonApiResponse<StudentPredictRes>>() {}
            );

            // ③ 处理返回结果（已修复200.equals错误）
            PythonApiResponse<StudentPredictRes> apiResponse = response.getBody();
            if (apiResponse == null || apiResponse.getCode() != 200) {
                throw new RuntimeException("Python单预测失败：" + (apiResponse != null ? apiResponse.getMsg() : "无响应"));
            }
            return apiResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用Python单预测API异常：" + e.getMessage());
        }
    }


    // 2. 调用Python批量Excel预测API（文件上传）
    // 替换PredictService中callBatchPredict方法的文件封装部分
    public BatchPredictRes callBatchPredict(MultipartFile excelFile) {
        try {
            // ① 校验文件（原有代码不变）
            if (excelFile == null || excelFile.isEmpty()) {
                throw new RuntimeException("请上传Excel文件");
            }
            String fileName = excelFile.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                throw new RuntimeException("仅支持.xlsx/.xls格式");
            }

            // ② 封装文件上传请求体（替换原有自定义Resource，用ByteArrayResource）
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            // 核心：将MultipartFile转为ByteArrayResource（Spring内置，无需自定义类）
            byte[] fileBytes = excelFile.getBytes();
            ByteArrayResource resource = new ByteArrayResource(fileBytes) {
                @Override
                public String getFilename() {
                    // 必须重写getFilename，否则Python端无法识别文件名
                    return excelFile.getOriginalFilename();
                }
            };
            requestBody.add("file", resource); // 字段名必须是"file"

            // ③ 设置请求头（原有代码不变）
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // ④ 调用Python API（原有代码不变）
            ResponseEntity<PythonApiResponse<BatchPredictRes>> response = restTemplate.exchange(
                    BATCH_PREDICT_URL,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<PythonApiResponse<BatchPredictRes>>() {}
            );

            // ⑤ 处理返回结果（原有代码不变）
            PythonApiResponse<BatchPredictRes> apiResponse = response.getBody();
            if (apiResponse == null || apiResponse.getCode() != 200) {
                throw new RuntimeException("Python批量预测失败：" + (apiResponse != null ? apiResponse.getMsg() : "无响应"));
            }
            return apiResponse.getData();
        } catch (Exception e) {
            // 打印完整异常堆栈，方便定位问题
            e.printStackTrace();
            throw new RuntimeException("调用Python批量预测API异常：" + e.getMessage());
        }
    }

}