package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.config.props.R2Props;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelStorageService {

    private final S3Client s3Client;
    private final R2Props props;

    public void uploadExcel(String key, byte[] bytes) {
        // create Excel workbook in memory
        try {
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(props.getBucket())
                    .key(key)
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .build();

            s3Client.putObject(putReq, RequestBody.fromBytes(bytes));
        } catch (Exception e) {
            log.error("Error while preparing to upload {}", e.getMessage());
            throw new RuntimeException("Report processing failed.");
        }
    }

    public byte[] downloadExcel(String key) {
        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .build();

        return s3Client.getObject(getReq, ResponseTransformer.toBytes()).asByteArray();
    }

    public void deleteExcel(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .build();
        s3Client.deleteObject(request);

    }
}

