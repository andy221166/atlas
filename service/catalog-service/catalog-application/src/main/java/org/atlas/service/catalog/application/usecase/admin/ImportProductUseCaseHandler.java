package org.atlas.service.catalog.application.usecase.admin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.atlas.platform.commons.enums.FileType;
import org.atlas.platform.commons.util.DateUtil;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.platform.storage.core.model.DownloadFileRequest;
import org.atlas.platform.storage.core.model.UploadFileRequest;
import org.atlas.platform.storage.core.service.StorageService;
import org.atlas.service.catalog.domain.entity.ProductEntity;
import org.atlas.service.catalog.port.inbound.admin.ImportProductUseCase;
import org.atlas.service.catalog.port.outbound.file.csv.ProductCsvReader;
import org.atlas.service.catalog.port.outbound.file.excel.ProductExcelReader;
import org.atlas.service.catalog.port.outbound.file.model.ImportProductItem;
import org.atlas.service.catalog.port.outbound.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportProductUseCaseHandler implements ImportProductUseCase {

    private static final int CHUNK_SIZE = 100;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(); // Use shared thread pool.

    private final ProductRepository productRepository;
    private final StorageService storageService;
    private final ProductCsvReader csvReader;
    private final ProductExcelReader excelReader;

    @Value("${app.storage.bucket}")
    private String bucket;

    @Override
    public Void handle(Input input) throws Exception {
        String storageObjectKey = getObjectKey(input);

        // Upload temp file
        storageService.upload(new UploadFileRequest(bucket, storageObjectKey, input.getFileContent()));

        EXECUTOR.submit(() -> {
            // Download temporary file
            byte[] fileContent = null;
            try {
                fileContent = storageService.download(new DownloadFileRequest(bucket, storageObjectKey));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Import products from file
            try {
                importFile(input.getFileType(), fileContent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return null;
    }

    private String getObjectKey(Input input) {
        String now = DateUtil.format(DateUtil.now(), "yyyyMMddHHmmss");
        return String.format("%s.%s", now, input.getFileType().getExtension());
    }

    private void importFile(FileType fileType, byte[] fileContent)
            throws IOException {
        ChunkConsumer chunkConsumer = new ChunkConsumer();
        switch (fileType) {
            case CSV -> csvReader.read(fileContent, CHUNK_SIZE, chunkConsumer);
            case EXCEL -> excelReader.read(fileContent, CHUNK_SIZE, chunkConsumer);
        }
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

        @Override
        public void accept(List<ImportProductItem> items) {
            // Count total number of items for logging
            log.info("Received chunk with {} items for processing.", items.size());

            // Classify items into their operation
            Map<Op, List<ProductEntity>> groupedItems = groupItemsByOp(items);

            processBatch(groupedItems.get(Op.INSERT), Op.INSERT,
                    productRepository::insertBatch);
            processBatch(groupedItems.get(Op.UPDATE), Op.UPDATE,
                    productRepository::updateBatch);
            processBatch(groupedItems.get(Op.DELETE), Op.DELETE,
                    productRepository::deleteBatch);
        }

        private Map<Op, List<ProductEntity>> groupItemsByOp(List<ImportProductItem> items) {
            return items.stream()
                    .collect(Collectors.groupingBy(
                            this::getActionType,
                            Collectors.mapping(item ->
                                            ObjectMapperUtil.getInstance().map(item, ProductEntity.class),
                                    Collectors.toList())
                    ));
        }

        private Op getActionType(ImportProductItem item) {
            if (Boolean.TRUE.equals(item.getDeleted())) {
                return Op.DELETE;
            } else if (item.getId() == null) {
                return Op.INSERT;
            } else {
                return Op.UPDATE;
            }
        }

        private void processBatch(List<ProductEntity> productEntities, Op op,
                                  Function<List<ProductEntity>, Integer> batchHandler) {
            if (productEntities == null || productEntities.isEmpty()) {
                log.info("No products to process for operation {}", op);
                return;
            }
            log.info("{} {} products...", op, productEntities.size());
            int imported = 0;
            try {
                imported = batchHandler.apply(productEntities);
            } catch (Exception e) {
                log.error("Error during batch operation {}", e.getMessage(), e);
            }
            log.info("{} operation completed: {}/{} products successfully processed.",
                    op, imported, productEntities.size());
        }
    }
}
