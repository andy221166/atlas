package org.atlas.platform.api.server.rest.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.platform.api.server.rest.response.Response;
import org.atlas.platform.commons.exception.AppError;
import org.atlas.platform.commons.exception.BusinessException;
import org.atlas.platform.i18n.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestExceptionHandler {

  private final MessageService messageService;

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Response<Void> handle(BusinessException e) {
    log.error("Occurred a business exception", e);
    String errorMessage = messageService.getMessage(e.getMessageCode(), "Unknown error");
    return Response.error(e.getErrorCode(), errorMessage);
  }

  /**
   * Invalid request body
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<Void> handle(MethodArgumentNotValidException e) {
    log.error("Invalid request", e);
    FieldError firstFieldError = e.getBindingResult().getFieldErrors().get(0);
    String message = String.format("[%s] %s", firstFieldError.getField(),
        firstFieldError.getDefaultMessage());
    return Response.error(AppError.BAD_REQUEST.getErrorCode(), message);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public final Response<Void> handle(HttpMessageNotReadableException e) {
    log.error("Invalid request", e);
    return Response.error(AppError.BAD_REQUEST.getErrorCode(), e.getMessage());
  }

  /**
   * Missing a required request parameter
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<Void> handle(MissingServletRequestParameterException e) {
    log.error("Invalid request", e);
    return Response.error(AppError.BAD_REQUEST.getErrorCode(),
        "Missing " + e.getParameterName());
  }
}
