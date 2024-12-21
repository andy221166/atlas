package org.atlas.platform.storage.s3.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.storage.core.model.DeleteFileRequest;
import org.atlas.platform.storage.core.model.DownloadFileRequest;
import org.atlas.platform.storage.core.model.UploadFileRequest;
import org.atlas.platform.storage.core.service.StorageService;
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
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

  private final S3Client s3Client;

  @Override
  public void upload(UploadFileRequest request) {
    if (shouldUseMultipartUpload(request)) {
      doGeneralUpload(request);
    } else {
      doMultipartUpload(request);
    }
  }

  private boolean shouldUseMultipartUpload(UploadFileRequest request) {
    return request.getFileContent().length / 1024 >= 100; // 100 MB
  }

  private void doGeneralUpload(UploadFileRequest request) {
    PutObjectRequest s3Request = PutObjectRequest.builder()
        .bucket(request.getBucket())
        .key(request.getFileName())
        .metadata(request.getMetadata())
        .build();
    try {
      s3Client.putObject(s3Request, RequestBody.fromBytes(request.getFileContent()));
      request.getCallback().onSuccess(null);
    } catch (Exception e) {
      request.getCallback().onFailure(e);
    }
  }

  private void doMultipartUpload(UploadFileRequest request) {
    try {
      // Initiate the multipart upload.
      CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
          .bucket(request.getBucket())
          .key(request.getFileName())
          .metadata(request.getMetadata())
          .build();
      CreateMultipartUploadResponse createMultipartUploadResponse = s3Client.createMultipartUpload(
          createMultipartUploadRequest);
      String uploadId = createMultipartUploadResponse.uploadId();

      int partNumber = 1;
      List<CompletedPart> completedParts = new ArrayList<>();
      ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024 * 5); // 5 MB byte buffer

      try (ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getFileContent())) {
        int bytesRead;
        while ((bytesRead = inputStream.read(byteBuffer.array())) != -1) {
          byteBuffer.limit(bytesRead);

          UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
              .bucket(request.getBucket())
              .key(request.getFileName())
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
          .bucket(request.getBucket())
          .key(request.getFileName())
          .uploadId(uploadId)
          .multipartUpload(completedMultipartUpload));

      request.getCallback().onSuccess(null);
    } catch (Exception e) {
      request.getCallback().onFailure(e);
    }
  }

  @Override
  public void download(DownloadFileRequest request) {
    GetObjectRequest s3Request = GetObjectRequest.builder()
        .bucket(request.getBucket())
        .key(request.getFileName())
        .build();
    try {
      InputStream inputStream = s3Client.getObject(s3Request, ResponseTransformer.toInputStream());
      try (inputStream) {
        byte[] fileContent = inputStream.readAllBytes();
        request.getCallback().onSuccess(fileContent);
      }
    } catch (Exception e) {
      request.getCallback().onFailure(e);
    }
  }

  @Override
  public void delete(DeleteFileRequest request) {
    DeleteObjectRequest s3Request = DeleteObjectRequest.builder()
        .bucket(request.getBucket())
        .key(request.getFileName())
        .build();
    try {
      s3Client.deleteObject(s3Request);
      request.getCallback().onSuccess(null);
    } catch (Exception e) {
      request.getCallback().onFailure(e);
    }
  }
}
