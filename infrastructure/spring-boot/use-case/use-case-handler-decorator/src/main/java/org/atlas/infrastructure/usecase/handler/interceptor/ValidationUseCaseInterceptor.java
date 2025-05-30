package org.atlas.infrastructure.usecase.handler.interceptor;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.framework.usecase.input.InternalInput;
import org.atlas.infrastructure.usecase.handler.exception.InvalidInputException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class ValidationUseCaseInterceptor implements UseCaseInterceptor {

  private final Validator validator;

  @Override
  public void preHandle(Class<?> useCaseClass, Object input) {
    if (input instanceof InternalInput internalInput) {
      if (internalInput.isValidationRequired()) {
        Set<ConstraintViolation<InternalInput>> violations = validator.validate(internalInput);
        if (!violations.isEmpty()) {
          List<String> errorMessages = violations.stream()
              .map(ConstraintViolation::getMessage)
              .toList();
          throw new InvalidInputException(errorMessages);
        }
      }
    }
  }

  @Override
  public void postHandle(Class<?> useCaseClass, Object input) {
    // Ignored
  }
}
