package org.atlas.platform.storage.s3.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.atlas.commons.function.Callback;
import org.atlas.platform.storage.contract.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;

@Service
public class S3StorageService implements StorageService {

  @Value("${app.storage.s3.bucket}")
  private String bucket;

  @Value("${app.storage.s3.general-upload-max-size:100}")
  private int generalUploadMaxSize;

  private final S3Client s3Client;

  public S3StorageService(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  @Override
  public void upload(String fileName, byte[] fileContent, Map<String, String> metadata,
      Callback<Void> callback) {
    if (fileContent.length / 1024 < generalUploadMaxSize) {
      doGeneralUpload(fileName, fileContent, metadata, callback);
    } else {
      doMultipartUpload(fileName, fileContent, metadata, callback);
    }
  }

  private void doGeneralUpload(String fileName, byte[] fileContent, Map<String, String> metadata,
      Callback<Void> callback) {
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucket)
        .key(fileName)
        .metadata(metadata)
        .build();
    try {
      s3Client.putObject(request, RequestBody.fromBytes(fileContent));
      callback.onSuccess(null);
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  private void doMultipartUpload(String fileName, byte[] fileContent, Map<String, String> metadata,
      Callback<Void> callback) {
    try {
      // Initiate the multipart upload.
      CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
          .bucket(bucket)
          .key(fileName)
          .metadata(metadata)
          .build();
      CreateMultipartUploadResponse createMultipartUploadResponse = s3Client.createMultipartUpload(
          createMultipartUploadRequest);
      String uploadId = createMultipartUploadResponse.uploadId();

      int partNumber = 1;
      List<CompletedPart> completedParts = new ArrayList<>();
      ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024 * 5); // 5 MB byte buffer

      try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent)) {
        int bytesRead;
        while ((bytesRead = inputStream.read(byteBuffer.array())) != -1) {
          byteBuffer.limit(bytesRead);

          UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
              .bucket(bucket)
              .key(fileName)
              .uploadId(uploadId)
              .partNumber(partNumber)
              .build();

          UploadPartResponse partResponse = s3Client.uploadPart(
              uploadPartRequest,
              RequestBody.fromByteBuffer(byteBuffer));

          CompletedPart part = CompletedPart.builder()
              .partNumber(partNumber)
              .eTag(partResponse.eTag())
              .build();
          completedParts.add(part);

          byteBuffer.clear();
          partNumber++;
        }
      }

      // Complete the multipart upload.
      CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
          .parts(completedParts)
          .build();
      s3Client.completeMultipartUpload(b -> b
          .bucket(bucket)
          .key(fileName)
          .uploadId(uploadId)
          .multipartUpload(completedMultipartUpload));

      callback.onSuccess(null);
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  @Override
  public void download(String fileName, Callback<byte[]> callback) {
    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(bucket)
        .key(fileName)
        .build();
    try {
      InputStream inputStream = s3Client.getObject(request, ResponseTransformer.toInputStream());
      try (inputStream) {
        byte[] fileContent = inputStream.readAllBytes();
        callback.onSuccess(fileContent);
      }
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  @Override
  public void delete(String fileName, Callback<Void> callback) {
    DeleteObjectRequest request = DeleteObjectRequest.builder()
        .bucket(bucket)
        .key(fileName)
        .build();
    try {
      s3Client.deleteObject(request);
      callback.onSuccess(null);
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }
}
