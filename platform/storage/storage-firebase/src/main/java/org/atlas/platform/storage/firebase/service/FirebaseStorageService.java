package org.atlas.platform.storage.firebase.service;

import com.google.cloud.storage.Blob;
import com.google.firebase.cloud.StorageClient;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.atlas.commons.function.Callback;
import org.atlas.platform.storage.contract.StorageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseStorageService implements StorageService {

  private final StorageClient storageClient;

  @Override
  public void upload(String fileName, byte[] fileContent, Map<String, String> metadata,
      Callback<Void> callback) {
    try {
      storageClient.bucket()
          .create(fileName, fileContent);
      callback.onSuccess(null);
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  @Override
  public void download(String fileName, Callback<byte[]> callback) {
    try {
      Blob blob = getBlob(fileName);
      if (blob == null) {
        throw new IOException("Blob not found");
      }
      byte[] fileContent = blob.getContent();
      callback.onSuccess(fileContent);
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  @Override
  public void delete(String fileName, Callback<Void> callback) {
    try {
      Blob blob = getBlob(fileName);
      if (blob == null) {
        throw new IOException("Blob not found");
      }
      blob.delete();
      callback.onSuccess(null);
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  private Blob getBlob(String fileName) {
    return storageClient.bucket().get(fileName);
  }
}
