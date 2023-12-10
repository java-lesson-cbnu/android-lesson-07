package kr.easw.lesson07.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import kr.easw.lesson07.model.dto.AWSKeyDto;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

@Service
public class AWSService {
    private static final String BUCKET_NAME = "easw-random-bucket-" + UUID.randomUUID();
    private AmazonS3 s3Client = null;

    public void initAWSAPI(AWSKeyDto awsKey) {
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKey.getApiKey(), awsKey.getApiSecretKey())))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
        for (Bucket bucket : s3Client.listBuckets()) {
            if (bucket.getName().startsWith("easw-random-bucket-")) {
                s3Client.listObjects(bucket.getName())
                        .getObjectSummaries()
                        .forEach(it -> s3Client.deleteObject(bucket.getName(), it.getKey()));
            }
        }
        s3Client.createBucket(BUCKET_NAME);
    }

    public boolean isInitialized() {
        return s3Client != null;
    }

    public List<String> getFileList() {
        return s3Client.listObjects(BUCKET_NAME).getObjectSummaries().stream().map(S3ObjectSummary::getKey).toList();
    }

    @SneakyThrows
    public void upload(MultipartFile file) {
        s3Client.putObject(BUCKET_NAME, file.getOriginalFilename(), new ByteArrayInputStream(file.getResource().getContentAsByteArray()), new ObjectMetadata());
    }
    @SneakyThrows
    public File downloadFile(String fileName) {
        if (!isInitialized()) {
            throw new IllegalStateException("AWS S3 클라이언트가 초기화되지 않았습니다.");
        }
        // S3에서 파일 다운로드
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, fileName);

        if (s3Object == null) {
            throw new IllegalArgumentException("다운로드할 파일이 존재하지 않습니다.");
        }

        try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
            File file = File.createTempFile("downloaded-", "-" + fileName);
            try (OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드 중 오류가 발생했습니다.", e);
        }
    }
}