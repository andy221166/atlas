package org.atlas.domain.product.usecase.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.domain.product.entity.BrandEntity;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.domain.product.entity.ProductAttributeEntity;
import org.atlas.domain.product.entity.ProductDetailEntity;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.repository.ProductRepository;
import org.atlas.domain.product.usecase.admin.AdminImportProductUseCaseHandler.ImportProductInput;
import org.atlas.framework.config.ApplicationConfigPort;
import org.atlas.framework.event.contract.product.ProductCreatedEvent;
import org.atlas.framework.event.publisher.ProductEventPublisherPort;
import org.atlas.framework.file.csv.ProductCsvReaderPort;
import org.atlas.framework.file.enums.FileType;
import org.atlas.framework.file.excel.ProductExcelReaderPort;
import org.atlas.framework.file.model.read.ProductRow;
import org.atlas.framework.objectmapper.ObjectMapperUtil;
import org.atlas.framework.storage.StoragePort;
import org.atlas.framework.storage.model.DeleteFileRequest;
import org.atlas.framework.storage.model.DownloadFileRequest;
import org.atlas.framework.storage.model.UploadFileRequest;
import org.atlas.framework.transaction.TransactionPort;
import org.atlas.framework.usecase.UseCaseHandler;
import org.atlas.framework.util.DateUtil;

@RequiredArgsConstructor
@Slf4j
public class AdminImportProductUseCaseHandler implements UseCaseHandler<ImportProductInput, Void> {

  private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

  private final ProductRepository productRepository;
  private final ApplicationConfigPort applicationConfigPort;
  private final ProductCsvReaderPort productCsvReaderPort;
  private final ProductExcelReaderPort productExcelReaderPort;
  private final ProductEventPublisherPort productEventPublisherPort;
  private final StoragePort storagePort;
  private final TransactionPort transactionPort;

  @Override
  public Void handle(ImportProductInput input) throws Exception {
    String storageObjectKey = getObjectKey(input);

    // Upload temp file
    storagePort.upload(
        new UploadFileRequest(applicationConfigPort.getProductImportBucket(), storageObjectKey,
            input.getFileContent()));

    EXECUTOR.submit(() -> {
      try {
        // Download temporary file
        byte[] fileContent = storagePort.download(
            new DownloadFileRequest(applicationConfigPort.getProductImportBucket(),
                storageObjectKey));

        // Read rows from file content
        List<ProductRow> rows;
        switch (input.getFileType()) {
          case CSV -> rows = productCsvReaderPort.read(fileContent);
          case EXCEL -> rows = productExcelReaderPort.read(fileContent);
          default -> throw new UnsupportedOperationException(
              "Unsupported file type: " + input.getFileType());
        }
        if (CollectionUtils.isEmpty(rows)) {
          log.info("No product to do insert");
          return;
        }

        // Sync into DB and publish events
        transactionPort.execute(() -> {
          List<ProductEntity> productEntities = rows.stream()
              .map(this::newProductEntity)
              .toList();
          productRepository.insertBatch(productEntities);
          productEntities.forEach(productEntity -> {
            ProductCreatedEvent event = new ProductCreatedEvent(
                applicationConfigPort.getApplicationName());
            ObjectMapperUtil.getInstance()
                .merge(productEntity, event);
            productEventPublisherPort.publish(event);
          });
        });
        log.info("Inserted {} products", rows.size());
      } catch (Exception e) {
        log.error("Occurred error while importing file {}", storageObjectKey, e);
      } finally {
        try {
          // Delete temporary file to clean
          storagePort.delete(
              new DeleteFileRequest(applicationConfigPort.getProductImportBucket(),
                  storageObjectKey));
        } catch (Exception e) {
          log.error("Failed to delete file {}", storageObjectKey, e);
        }
      }
    });

    return null;
  }

  private String getObjectKey(ImportProductInput input) {
    String now = DateUtil.now("yyyyMMddHHmmss");
    return String.format("%s.%s", now, input.getFileType().getExtension());
  }

  private ProductEntity newProductEntity(ProductRow row) {
    // SearchResponse
    ProductEntity productEntity = ObjectMapperUtil.getInstance().map(row, ProductEntity.class);

    // SearchResponse detail
    ProductDetailEntity productDetailEntity = new ProductDetailEntity();
    productDetailEntity.setDescription(row.getDescription());
    productEntity.setDetail(productDetailEntity);

    // SearchResponse attributes
    ProductAttributeEntity productAttributeEntity1 = new ProductAttributeEntity();
    productAttributeEntity1.setName(row.getAttributeName1());
    productAttributeEntity1.setValue(row.getAttributeValue1());
    productEntity.addAttribute(productAttributeEntity1);

    ProductAttributeEntity productAttributeEntity2 = new ProductAttributeEntity();
    productAttributeEntity2.setName(row.getAttributeName2());
    productAttributeEntity2.setValue(row.getAttributeValue2());
    productEntity.addAttribute(productAttributeEntity2);

    ProductAttributeEntity productAttributeEntity3 = new ProductAttributeEntity();
    productAttributeEntity3.setName(row.getAttributeName3());
    productAttributeEntity3.setValue(row.getAttributeValue3());
    productEntity.addAttribute(productAttributeEntity3);

    // Brand
    BrandEntity brandEntity = new BrandEntity();
    brandEntity.setId(row.getBrandId());
    productEntity.setBrand(brandEntity);

    // Categories
    List<CategoryEntity> categoryEntities = row.getCategoryIds()
        .stream()
        .map(categoryId -> {
          CategoryEntity categoryEntity = new CategoryEntity();
          categoryEntity.setId(categoryId);
          return categoryEntity;
        })
        .toList();
    productEntity.setCategories(categoryEntities);

    return productEntity;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ImportProductInput {

    @NotNull
    private FileType fileType;

    @NotEmpty
    private byte[] fileContent;
  }
}
