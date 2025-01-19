package org.atlas.platform.storage.s3.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.atlas.platform.storage.core.model.DeleteFileRequest;
import org.atlas.platform.storage.core.model.DownloadFileRequest;
import org.atlas.platform.storage.core.model.GetDownloadUrlRequest;
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
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Override
  public void upload(UploadFileRequest request) throws Exception {
    if (shouldUseMultipartUpload(request)) {
      doGeneralUpload(request);
    } else {
      doMultipartUpload(request);
    }
  }

  private boolean shouldUseMultipartUpload(UploadFileRequest request) {
    return request.getContent().length / 1024 >= 100; // 100 MB
  }

  private void doGeneralUpload(UploadFileRequest request) {
    PutObjectRequest s3Request = PutObjectRequest.builder()
        .bucket(request.getBucket())
        .key(request.getObjectKey())
        .metadata(request.getMetadata())
        .build();
    s3Client.putObject(s3Request, RequestBody.fromBytes(request.getContent()));
  }

  private void doMultipartUpload(UploadFileRequest request) throws IOException {
    // Initiate the multipart upload
    CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
        .bucket(request.getBucket())
        .key(request.getObjectKey())
        .metadata(request.getMetadata())
        .build();
    CreateMultipartUploadResponse createMultipartUploadResponse = s3Client.createMultipartUpload(
        createMultipartUploadRequest);
    String uploadId = createMultipartUploadResponse.uploadId();

    int partNumber = 1;
    List<CompletedPart> completedParts = new ArrayList<>();
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024 * 5); // 5 MB byte buffer

    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getContent())) {
      int bytesRead;
      while ((bytesRead = inputStream.read(byteBuffer.array())) != -1) {
        byteBuffer.limit(bytesRead);

        UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
            .bucket(request.getBucket())
            .key(request.getObjectKey())
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

    // Complete the multipart upload
    CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
        .parts(completedParts)
        .build();
    s3Client.completeMultipartUpload(b -> b
        .bucket(request.getBucket())
        .key(request.getObjectKey())
        .uploadId(uploadId)
        .multipartUpload(completedMultipartUpload));
  }

  @Override
  public byte[] download(DownloadFileRequest request) throws Exception {
    GetObjectRequest s3Request = GetObjectRequest.builder()
        .bucket(request.getBucket())
        .key(request.getObjectKey())
        .build();
    try (InputStream inputStream = s3Client.getObject(s3Request,
        ResponseTransformer.toInputStream())) {
      return inputStream.readAllBytes();
    }
  }

  @Override
  public String getDownloadUrl(GetDownloadUrlRequest request) throws Exception {
    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(b -> b.bucket(request.getBucket()).key(request.getObjectKey()))
        .signatureDuration(request.getTtl())
        .build();
    PresignedGetObjectRequest presignedGetObjectRequest =
        s3Presigner.presignGetObject(getObjectPresignRequest);
    return presignedGetObjectRequest.url().toString();
  }

  @Override
  public void delete(DeleteFileRequest request) throws Exception {
    DeleteObjectRequest s3Request = DeleteObjectRequest.builder()
        .bucket(request.getBucket())
        .key(request.getObjectKey())
        .build();
    s3Client.deleteObject(s3Request);
  }
}
