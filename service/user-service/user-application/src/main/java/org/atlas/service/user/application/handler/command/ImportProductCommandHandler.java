package org.atlas.service.user.application.handler.command;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.atlas.commons.enums.FileType;
import org.atlas.commons.util.UUIDGenerator;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.storage.core.model.DownloadFileRequest;
import org.atlas.platform.storage.core.model.UploadFileRequest;
import org.atlas.platform.storage.core.service.StorageService;
import org.atlas.service.auth.application.Constant;
import org.atlas.service.product.contract.file.csv.ProductCsvReader;
import org.atlas.service.product.contract.file.excel.ProductExcelReader;
import org.atlas.service.product.contract.repository.ImportProductRequestRepository;
import org.atlas.service.product.contract.repository.ProductRepository;
import org.atlas.service.product.domain.ImportProductItem;
import org.atlas.service.product.domain.ImportProductRequest;
import org.atlas.service.product.domain.ImportProductRequestStatus;
import org.atlas.service.product.domain.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportProductCommandHandler implements CommandHandler<ImportProductCommand, Integer> {

  private static final int CHUNK_SIZE = 100;
  private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(); // Use shared thread pool.

  private final ImportProductRequestRepository requestRepository;
  private final ProductRepository productRepository;
  private final StorageService storageService;
  private final ProductCsvReader csvReader;
  private final ProductExcelReader excelReader;

  @Value("${app.storage.bucket}")
  private String bucket;

  @Override
  public Integer handle(ImportProductCommand command) throws Exception {
    ImportProductRequest request = initializeRequest();

    // Safely handle file upload
    try {
      uploadTempFile(request.getObjectKey(), command.getFileContent());
    } catch (Exception e) {
      log.error("Import product request {}: Failed to upload temp file", request.getId());
      updateRequestStatus(request, ImportProductRequestStatus.CANCELED, e.getMessage());
      throw e; // Do not suppress exceptions, rethrow for transparency
    }

    // Process the file asynchronously using a reusable `processFileAsync` utility method
    processFileAsync(request, command);

    return request.getId();
  }

  private ImportProductRequest initializeRequest() {
    ImportProductRequest request = new ImportProductRequest();
    request.setStatus(ImportProductRequestStatus.NEW);
    request.setObjectKey(UUIDGenerator.generate());
    request.setTotalImported(0);
    request.setTotalProcessed(0);
    requestRepository.save(request); // Persist request in the repository
    return request;
  }

  private void uploadTempFile(String objectKey, byte[] fileContent) throws Exception {
    storageService.upload(new UploadFileRequest(bucket, objectKey, fileContent));
  }

  private void processFileAsync(ImportProductRequest request, ImportProductCommand command) {
    EXECUTOR.submit(() -> {
      // Download temporary file
      byte[] fileContent;
      try {
        fileContent = downloadTempFile(request.getObjectKey());
      } catch (Exception e) {
        log.error("Import product request {}: Failed to download temp file", request.getId());
        updateRequestStatus(request, ImportProductRequestStatus.CANCELED, e.getMessage());
        throw new RuntimeException(e);
      }

      // Import products from file
      try {
        Pair<Integer, Integer> importResults = importFile(command.getFileType(), fileContent);
        log.info("Import product request {}: Imported done {}/{}",
            request.getId(), importResults.getLeft(), importResults.getRight());
        updateRequestStatus(request, ImportProductRequestStatus.COMPLETED, importResults.getLeft(),
            importResults.getRight(), null);
      } catch (Exception e) {
        log.error("Import product request {}: Failed to import", request.getId());
        updateRequestStatus(request, ImportProductRequestStatus.CANCELED, e.getMessage());
        throw new RuntimeException(e);
      }
    });
  }

  private byte[] downloadTempFile(String objectKey) throws Exception {
    return storageService.download(new DownloadFileRequest(bucket, objectKey));
  }

  private Pair<Integer, Integer> importFile(FileType fileType, byte[] fileContent)
      throws IOException {
    ChunkConsumer chunkConsumer = new ChunkConsumer();
    switch (fileType) {
      case CSV -> csvReader.read(fileContent, CHUNK_SIZE, chunkConsumer);
      case EXCEL ->
          excelReader.read(fileContent, org.atlas.service.user.application.Constant.EXCEL_SHEET_NAME, CHUNK_SIZE, chunkConsumer);
    }
    ;
    return Pair.of(chunkConsumer.totalImported, chunkConsumer.totalProcessed);
  }

  private void updateRequestStatus(ImportProductRequest request, ImportProductRequestStatus status,
      String error) {
    updateRequestStatus(request, status, null, null, error);
  }

  private void updateRequestStatus(ImportProductRequest request, ImportProductRequestStatus status,
      Integer totalImported, Integer totalProcessed, String error) {
    request.setStatus(status);
    if (totalImported != null) {
      request.setTotalImported(totalImported);
    }
    if (totalProcessed != null) {
      request.setTotalProcessed(totalProcessed);
    }
    if (error != null) {
      request.setError(error);
    }
    requestRepository.save(request);
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Getter
  private enum Op {
    INSERT("Insert"),
    UPDATE("Update"),
    DELETE("Delete");

    private final String name;
  }

  private class ChunkConsumer implements Consumer<List<ImportProductItem>> {

    private Integer totalImported;
    private Integer totalProcessed;

    @Override
    public void accept(List<ImportProductItem> items) {
      // Count total number of items for logging
      log.info("Received chunk with {} items for processing.", items.size());
      totalImported += items.size();

      // Classify items into their operation
      Map<Op, List<Product>> groupedItems = groupItemsByOp(items);

      processBatch(groupedItems.get(Op.INSERT), Op.INSERT,
          productRepository::insertBatch);
      processBatch(groupedItems.get(Op.UPDATE), Op.UPDATE,
          productRepository::updateBatch);
      processBatch(groupedItems.get(Op.DELETE), Op.DELETE,
          productRepository::deleteBatch);
    }

    private Map<Op, List<Product>> groupItemsByOp(List<ImportProductItem> items) {
      return items.stream()
          .collect(Collectors.groupingBy(
              this::getActionType,
              Collectors.mapping(item ->
                      ObjectMapperUtil.getInstance().map(item, Product.class),
                  Collectors.toList())
          ));
    }

    private Op getActionType(ImportProductItem item) {
      if (Boolean.TRUE.equals(item.getDelete())) {
        return Op.DELETE;
      } else if (item.getId() == null) {
        return Op.INSERT;
      } else {
        return Op.UPDATE;
      }
    }

    private void processBatch(List<Product> products, Op op,
        Function<List<Product>, Integer> batchHandler) {
      if (products == null || products.isEmpty()) {
        log.info("No products to process for operation {}", op);
        return;
      }

      log.info("{} {} products...", op, products.size());
      int processed = 0;
      try {
        processed = batchHandler.apply(products);
      } catch (Exception e) {
        log.error("Error during batch operation {}", e.getMessage(), e);
      }
      log.info("{} operation completed: {}/{} products successfully processed.",
          op, processed, products.size());
      totalProcessed += processed;
    }
  }
}
