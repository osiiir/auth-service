package com.osir.authservice.handler;


import com.osir.authservice.exception.BaseException;
import com.osir.takeoutpojo.constant.ErrorMessageConstant;
import com.osir.takeoutpojo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error("异常信息：{}", ex.getMessage());
        String meg = ex.getMessage();
        if(meg.contains("Duplicate entry")){
            String[] s = meg.split(" ");
            return Result.error(s[2]+ ErrorMessageConstant.ALREADY_EXISTS);
        }
        else return Result.error(ErrorMessageConstant.UNKNOWN_ERROR);
    }


}
