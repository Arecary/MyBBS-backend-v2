package org.bbsv2.common.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.bbsv2.common.Result;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();


    //统一异常处理@ExceptionHandler,主要用于Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json串
    public Result error(HttpServletRequest request, Exception e){
        log.error("GlobalExceptionHandler 捕获到未知异常：", e);
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody//返回json串
    public Result customError(HttpServletRequest request, CustomException e){
        log.error("GlobalExceptionHandler 捕获到自定义异常：code={}, msg={}", e.getCode(), e.getMsg());
        return Result.error(e.getCode(), e.getMsg());
    }
}
