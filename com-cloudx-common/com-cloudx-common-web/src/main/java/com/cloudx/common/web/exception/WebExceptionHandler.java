package com.cloudx.common.web.exception;

import com.cloudx.common.entity.error.CodeMsg;
import com.cloudx.common.entity.error.ServiceException;
import com.cloudx.common.entity.response.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理，返回统一格式的错误信息
 * @author heycm
 * @version 1.0
 * @since 2024/11/16 13:03
 */
@RestControllerAdvice
@Slf4j
public class WebExceptionHandler {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public Result<Object> error(Throwable e) {
        log.error("Throwable 发生未知异常！", e);
        return Result.error(CodeMsg.ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<Object> error(Exception e) {
        log.error("Exception 发生未知异常！", e);
        return Result.error(CodeMsg.ERROR);
    }

    @ExceptionHandler(ServiceException.class)
    public Result<Object> error(ServiceException e) {
        return Result.error(e.code, e.getMessage());
    }

    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public Result<Object> handleValidatedException(Exception e) {
        String message;
        if (e instanceof MethodArgumentNotValidException) {
            // BeanValidation exception
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            message = getParamError(ex.getBindingResult());
        } else if (e instanceof ConstraintViolationException) {
            // BeanValidation GET simple param
            ConstraintViolationException ex = (ConstraintViolationException) e;
            message = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; "));
        } else if (e instanceof BindException) {
            // BeanValidation GET object param
            BindException ex = (BindException) e;
            message = getParamError(ex.getBindingResult());
        } else {
            message = CodeMsg.INVALID_PARAM.message();
        }

        return Result.error(CodeMsg.INVALID_PARAM.code(), message);
    }

    private String getParamError(BindingResult br) {
        List<FieldError> fieldErrors = br.getFieldErrors();
        if (CollectionUtils.isEmpty(fieldErrors)) {
            return "参数错误";
        }
        StringJoiner joiner = new StringJoiner(";");
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            if (message != null && message.contains("java.lang.NumberFormatException")) {
                message = "参数类型异常";
            }
            joiner.add(field + ":" + message);
        }
        return joiner.toString();
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public Result<Object> error(HandlerMethodValidationException e) {
        if (!CollectionUtils.isEmpty(e.getAllValidationResults()) && e.getAllValidationResults().getFirst() != null) {
            ParameterValidationResult first = e.getAllValidationResults().getFirst();
            int parameterIndex = first.getMethodParameter().getParameterIndex();
            Parameter parameter = e.getMethod().getParameters()[parameterIndex];
            String parameterName = parameter.getName();
            String defaultMessage = first.getResolvableErrors().getFirst().getDefaultMessage();
            return Result.error(CodeMsg.INVALID_PARAM.code(), parameterName + ":" + defaultMessage);
        }
        return Result.error(CodeMsg.INVALID_PARAM);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Object> error(MissingServletRequestParameterException e) {
        return Result.error(CodeMsg.INVALID_PARAM.code(), "缺少参数: " + e.getParameterName());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> error(HttpRequestMethodNotSupportedException e) {
        return Result.error(405, "不支持的请求方式: " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Object> error(NoResourceFoundException e) {
        return Result.error(CodeMsg.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Object> error(MaxUploadSizeExceededException e) {
        return Result.error(CodeMsg.INVALID_PARAM.code(), e.getMessage());
    }
}
