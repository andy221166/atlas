package org.atlas.platform.storage.core.service;

import org.atlas.platform.storage.core.model.DeleteFileRequest;
import org.atlas.platform.storage.core.model.DownloadFileRequest;
import org.atlas.platform.storage.core.model.GetDownloadUrlRequest;
import org.atlas.platform.storage.core.model.UploadFileRequest;

public interface StorageService {

  void upload(UploadFileRequest request) throws Exception;

  byte[] download(DownloadFileRequest request) throws Exception;

  String getDownloadUrl(GetDownloadUrlRequest request) throws Exception;

  void delete(DeleteFileRequest request) throws Exception;
}
