package org.atlas.platform.storage.filesystem.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.storage.core.model.BaseRequest;
import org.atlas.platform.storage.core.model.DeleteFileRequest;
import org.atlas.platform.storage.core.model.DownloadFileRequest;
import org.atlas.platform.storage.core.model.GetDownloadUrlRequest;
import org.atlas.platform.storage.core.model.UploadFileRequest;
import org.atlas.platform.storage.core.service.StorageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileSystemStorageService implements StorageService {

  @Override
  public void upload(UploadFileRequest request) throws Exception {
    Path filePath = toFilePath(request);
    checkParentDirectories(filePath);

    // Write file content
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(request.getFileContent());
    }
    log.info("Uploaded file successfully: {}", filePath);
  }

  @Override
  public byte[] download(DownloadFileRequest request) throws Exception {
    Path filePath = toFilePath(request);
    checkFileExists(filePath);

    // Read and return file content
    byte[] content = Files.readAllBytes(filePath);
    log.info("Downloaded file successfully: {}", filePath);
    return content;
  }

  @Override
  public String getDownloadUrl(GetDownloadUrlRequest request) throws Exception {
    Path filePath = toFilePath(request);
    checkFileExists(filePath);

    // Simulate a download URL
    String downloadUrl = "file://" + filePath.toAbsolutePath();
    log.info("Generated download URL: {}", downloadUrl);
    return downloadUrl;
  }

  @Override
  public void delete(DeleteFileRequest request) throws Exception {
    Path filePath = toFilePath(request);
    checkFileExists(filePath);

    // Attempt to delete the file
    Files.delete(filePath);

    log.info("Deleted file successfully: {}", filePath);
  }

  private Path toFilePath(BaseRequest request) {
    return Paths.get(request.getBucket(), request.getObjectKey());
  }

  private void checkParentDirectories(Path filePath) throws IOException {
    Path parentDir = filePath.getParent();
    if (parentDir != null && !Files.exists(parentDir)) {
      Files.createDirectories(parentDir);
      log.info("Created parent directories: {}", parentDir);
    }
  }

  private void checkFileExists(Path filePath) throws FileNotFoundException {
    if (!Files.exists(filePath)) {
      throw new FileNotFoundException("File not found: " + filePath);
    }
  }
}
