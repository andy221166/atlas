package org.atlas.framework.storage;

import org.atlas.framework.storage.model.DeleteFileRequest;
import org.atlas.framework.storage.model.DownloadFileRequest;
import org.atlas.framework.storage.model.GetDownloadUrlRequest;
import org.atlas.framework.storage.model.UploadFileRequest;

public interface StoragePort {

  void upload(UploadFileRequest request) throws Exception;

  byte[] download(DownloadFileRequest request) throws Exception;

  String getDownloadUrl(GetDownloadUrlRequest request) throws Exception;

  void delete(DeleteFileRequest request) throws Exception;
}
