package com.redbook.redbook.exceptio;


import com.redbook.redbook.vo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.error("系统错误：" + e.getMessage());
    }
}