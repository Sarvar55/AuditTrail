package com.codems.audittrail.common.exception.handler;

import com.codems.audittrail.common.base.BaseResponse;
import com.codems.audittrail.common.exception.model.ErrorResponse;
import com.codems.audittrail.common.exception.model.FieldErrorResponse;
import com.codems.audittrail.common.exception.type.BaseException;
import com.codems.audittrail.common.exception.type.ValidationException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleBaseException(BaseException exception) {

        ErrorResponse error = exception instanceof ValidationException validationException
                ? new ErrorResponse(exception.getCode(), exception.getMessage(), validationException.getFieldErrors())
                : ErrorResponse.of(exception.getCode(), exception.getMessage());

        return ResponseEntity
                .status(exception.getStatus())
                .body(BaseResponse.error(error, exception.getStatus(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleValidation(MethodArgumentNotValidException exception) {

        List<FieldErrorResponse> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        return handleBaseException(new ValidationException(fieldErrors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleConstraintViolation(ConstraintViolationException exception) {
        List<FieldErrorResponse> fieldErrors = exception.getConstraintViolations().stream()
                .map(error -> new FieldErrorResponse(error.getPropertyPath().toString(), error.getMessage()))
                .toList();
        return handleBaseException(new ValidationException(fieldErrors));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<BaseResponse<ErrorResponse>> handleMalformedRequest(Exception exception) {
        return handleBaseException(new ValidationException(List.of(
                new FieldErrorResponse(null, "Request contains an invalid value")
        )));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<ErrorResponse>> handleUnexpected(Exception exception) {
        String message = "An unexpected error occurred";
        log.error("Unhandled application error", exception);
        ErrorResponse error = ErrorResponse.of("INTERNAL_SERVER_ERROR", message);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error(error, HttpStatus.INTERNAL_SERVER_ERROR, message));
    }
}
