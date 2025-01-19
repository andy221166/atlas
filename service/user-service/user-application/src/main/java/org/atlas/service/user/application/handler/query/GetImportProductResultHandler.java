package org.atlas.service.user.application.handler.query;

import lombok.RequiredArgsConstructor;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;
import org.atlas.platform.cqrs.handler.QueryHandler;
import org.atlas.platform.objectmapper.ObjectMapperUtil;
import org.atlas.service.product.contract.model.ImportProductResultDto;
import org.atlas.service.product.contract.query.GetImportProductResultQuery;
import org.atlas.service.product.contract.repository.ImportProductRequestRepository;
import org.atlas.service.product.domain.ImportProductRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetImportProductResultHandler implements
    QueryHandler<GetImportProductResultQuery, ImportProductResultDto> {

  private final ImportProductRequestRepository repository;

  @Override
  @Transactional(readOnly = true)
  public ImportProductResultDto handle(GetImportProductResultQuery query) throws Exception {
    ImportProductRequest request = repository.findById(query.getRequestId())
        .orElseThrow(() -> new BusinessException(AppError.IMPORT_PRODUCT_REQUEST_NOT_FOUND));
    return ObjectMapperUtil.getInstance().map(request, ImportProductResultDto.class);
  }
}
