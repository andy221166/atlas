package org.atlas.platform.storage.core.service;

import org.atlas.platform.storage.core.model.DeleteFileRequest;
import org.atlas.platform.storage.core.model.DownloadFileRequest;
import org.atlas.platform.storage.core.model.UploadFileRequest;

public interface StorageService {

  void upload(UploadFileRequest request);

  void download(DownloadFileRequest request);

  void delete(DeleteFileRequest request);
}
