package com.guli.core.handler;

import com.guli.core.common.R;
import com.guli.core.common.ResponseCode;
import com.guli.core.common.ServerResponse;
import com.guli.core.exception.GuliException;
import com.guli.core.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理类
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /*@ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error();
    }*/

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResponse error(HttpServletRequest request, Exception e){
        log.error("{} 请求发生了exception异常",request.getRequestURI(),e);
//        log.error(ExceptionUtil.getMessage(e));
        return ServerResponse.createByErrorMessage(e.toString());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ServerResponse error(HttpServletRequest request, RuntimeException e){
        log.error("{} 请求发生了exception异常",request.getRequestURI(),e);
        return ServerResponse.createByErrorMessage(e.toString());
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    public ServerResponse error(HttpServletRequest request,  BadSqlGrammarException e){
        log.error("{} 请求发生了exception异常",request.getRequestURI(),e);
        return ServerResponse.createByErrorCodeMessage(ResponseCode.BAD_SQL_GRAMMAR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ServerResponse error(HttpServletRequest request, HttpMessageNotReadableException e){
        log.error("{} 请求发生了exception异常",request.getRequestURI(),e);
        return ServerResponse.createByErrorCodeMessage(ResponseCode.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public ServerResponse error(HttpServletRequest request, GuliException e){
        log.error("{} 请求发生了exception异常",request.getRequestURI(),e);
        return ServerResponse.createByErrorCodeMessage(e.getCode(),e.getMessage());
//        return R.error().message(e.getMessage()).code(e.getCode());
    }
}