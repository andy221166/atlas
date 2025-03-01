package org.atlas.service.product.application.usecase.admin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.atlas.platform.commons.util.DateUtil;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.sequencegenerator.SequenceGenerator;
import org.atlas.platform.storage.core.model.DeleteFileRequest;
import org.atlas.platform.storage.core.model.DownloadFileRequest;
import org.atlas.platform.storage.core.model.UploadFileRequest;
import org.atlas.platform.storage.core.service.StorageService;
import org.atlas.service.product.domain.entity.BrandEntity;
import org.atlas.service.product.domain.entity.CategoryEntity;
import org.atlas.service.product.domain.entity.ProductDetailEntity;
import org.atlas.service.product.domain.entity.ProductEntity;
import org.atlas.service.product.domain.entity.ProductImageEntity;
import org.atlas.platform.event.contract.product.ProductCreatedEvent;
import org.atlas.platform.event.contract.product.ProductDeletedEvent;
import org.atlas.platform.event.contract.product.ProductUpdatedEvent;
import org.atlas.service.product.port.inbound.admin.ImportProductUseCase;
import org.atlas.service.product.port.outbound.event.publisher.ProductEventPublisher;
import org.atlas.service.product.port.outbound.file.csv.ProductCsvReader;
import org.atlas.service.product.port.outbound.file.excel.ProductExcelReader;
import org.atlas.service.product.port.outbound.file.model.read.ProductRow;
import org.atlas.service.product.port.outbound.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportProductUseCaseHandler implements ImportProductUseCase {

  private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

  private final ProductRepository productRepository;
  private final StorageService storageService;
  private final ProductCsvReader productCsvReader;
  private final ProductExcelReader productExcelReader;
  private final TransactionTemplate transactionTemplate;
  private final SequenceGenerator sequenceGenerator;
  private final ProductEventPublisher productEventPublisher;

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${app.storage.bucket}")
  private String bucket;

  @Override
  public Void handle(Input input) throws Exception {
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
          case CSV -> rows = productCsvReader.read(fileContent);
          case EXCEL -> rows = productExcelReader.read(fileContent);
          default -> throw new UnsupportedOperationException(
              "Unsupported file type: " + input.getFileType());
        }

        // Classify rows into their operation
        Map<Op, List<ProductRow>> groupedRows = rows.stream()
            .collect(Collectors.groupingBy(this::getActionType));

        // Sync into DB and publish events
        doInsert(groupedRows.get(Op.INSERT));
        doUpdate(groupedRows.get(Op.UPDATE));
        doDelete(groupedRows.get(Op.DELETE));
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

  private String getObjectKey(Input input) {
    String now = DateUtil.now("yyyyMMddHHmmss");
    return String.format("%s.%s", now, input.getFileType().getExtension());
  }

  private Op getActionType(ProductRow item) {
    if (Boolean.TRUE.equals(item.getDeleted())) {
      return Op.DELETE;
    } else if (item.getId() == null) {
      return Op.INSERT;
    } else {
      return Op.UPDATE;
    }
  }

  private void doInsert(List<ProductRow> rows) {
    if (CollectionUtils.isEmpty(rows)) {
      log.info("No product to do insert");
      return;
    }
    transactionTemplate.executeWithoutResult((status) -> {
      List<ProductEntity> productEntities = rows.stream()
          .map(this::newProductEntity)
          .toList();
      productRepository.insertBatch(productEntities);
      productEntities.forEach(productEntity -> {
        ProductCreatedEvent event = new ProductCreatedEvent(applicationName);
        ObjectMapperUtil.getInstance().merge(productEntity, event);
        productEventPublisher.publish(event);
      });
    });
    log.info("Inserted {} products", rows.size());
  }

  private void doUpdate(List<ProductRow> rows) {
    if (CollectionUtils.isEmpty(rows)) {
      log.info("No row to do update");
      return;
    }
    transactionTemplate.executeWithoutResult((status) -> {
      List<ProductEntity> productEntities = rows.stream()
          .map(this::newProductEntity)
          .toList();
      productRepository.updateBatch(productEntities);
      productEntities.forEach(productEntity -> {
        ProductUpdatedEvent event = new ProductUpdatedEvent(applicationName);
        ObjectMapperUtil.getInstance().merge(productEntity, event);
        productEventPublisher.publish(event);
      });
    });
    log.info("Updated {} products", rows.size());
  }

  private void doDelete(List<ProductRow> rows) {
    if (CollectionUtils.isEmpty(rows)) {
      log.info("No row to do delete");
      return;
    }
    transactionTemplate.executeWithoutResult((status) -> {
      List<ProductEntity> productEntities = rows.stream()
          .map(this::newProductEntity)
          .toList();
      productRepository.deleteBatch(productEntities);
      productEntities.forEach(productEntity -> {
        ProductDeletedEvent event = new ProductDeletedEvent(applicationName);
        event.setId(productEntity.getId());
        productEventPublisher.publish(event);
      });
    });
    log.info("Deleted {} products", rows.size());
  }

  private ProductEntity newProductEntity(ProductRow row) {
    // Product
    ProductEntity productEntity = ObjectMapperUtil.getInstance().map(row, ProductEntity.class);

    if (getActionType(row) == Op.INSERT) {
      productEntity.setCode(sequenceGenerator.generate("product", "PRD", 7));
    }

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

    // Product detail
    ProductDetailEntity productDetailEntity = new ProductDetailEntity();
    productDetailEntity.setDescription(row.getDescription());
    productEntity.setDetail(productDetailEntity);

    // Product images
    ProductImageEntity productImageEntity = new ProductImageEntity();
    productImageEntity.setImageUrl(row.getImageUrl());
    productImageEntity.setIsCover(true);
    productEntity.setImages(Collections.singletonList(productImageEntity));

    return productEntity;
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @Getter
  private enum Op {

    INSERT("Insert"),
    UPDATE("Update"),
    DELETE("Delete");

    private final String name;
  }
}
