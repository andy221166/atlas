package org.atlas.service.product.application.admin;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.util.DateUtil;
import org.atlas.platform.config.ApplicationConfigService;
import org.atlas.platform.event.contract.product.ProductCreatedEvent;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.storage.core.model.DeleteFileRequest;
import org.atlas.platform.storage.core.model.DownloadFileRequest;
import org.atlas.platform.storage.core.model.UploadFileRequest;
import org.atlas.platform.storage.core.service.StorageService;
import org.atlas.domain.product.entity.BrandEntity;
import org.atlas.domain.product.entity.CategoryEntity;
import org.atlas.domain.product.entity.ProductDetailEntity;
import org.atlas.domain.product.entity.ProductEntity;
import org.atlas.domain.product.entity.ProductAttributeEntity;
import org.atlas.port.inbound.product.admin.ImportProductUseCase;
import org.atlas.service.product.port.outbound.event.ProductEventPublisherPort;
import org.atlas.service.product.port.outbound.file.csv.ProductCsvReaderPort;
import org.atlas.service.product.port.outbound.file.excel.ProductExcelReaderPort;
import org.atlas.service.product.port.outbound.file.model.read.ProductRow;
import org.atlas.service.product.port.outbound.repository.ProductRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportProductUseCaseHandler implements ImportProductUseCase {

  private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

  private final ProductRepositoryPort productRepositoryPort;
  private final StorageService storageService;
  private final ProductCsvReaderPort productCsvReaderPort;
  private final ProductExcelReaderPort productExcelReaderPort;
  private final TransactionTemplate transactionTemplate;
  private final ApplicationConfigService applicationConfigService;
  private final ProductEventPublisherPort productEventPublisherPort;

  @Value("${app.storage.bucket}")
  private String bucket;

  @Override
  public Void handle(ImportProductInput input) throws Exception {
    String storageObjectKey = getObjectKey(input);

    // Upload temp file
    storageService.upload(new UploadFileRequest(bucket, storageObjectKey, input.getFileContent()));

    EXECUTOR.submit(() -> {
      try {
        // Download temporary file
        byte[] fileContent = storageService.download(
            new DownloadFileRequest(bucket, storageObjectKey));

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
        transactionTemplate.executeWithoutResult((status) -> {
          List<ProductEntity> productEntities = rows.stream()
              .map(this::newProductEntity)
              .toList();
          productRepositoryPort.insertBatch(productEntities);
          productEntities.forEach(productEntity -> {
            ProductCreatedEvent event = new ProductCreatedEvent(
                applicationConfigService.getApplicationName());
            ObjectMapperUtil.getInstance().merge(productEntity, event);
            productEventPublisherPort.publish(event);
          });
        });
        log.info("Inserted {} products", rows.size());
      } catch (Exception e) {
        log.error("Occurred error while importing file {}", storageObjectKey, e);
      } finally {
        try {
          // Delete temporary file to clean
          storageService.delete(new DeleteFileRequest(bucket, storageObjectKey));
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
    // Product
    ProductEntity productEntity = ObjectMapperUtil.getInstance().map(row, ProductEntity.class);

    // Product detail
    ProductDetailEntity productDetailEntity = new ProductDetailEntity();
    productDetailEntity.setDescription(row.getDescription());
    productEntity.setDetail(productDetailEntity);

    // Product attributes
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
}
