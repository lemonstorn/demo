package com.weaver.web.config;

import com.weaver.core.model.WeaverResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author: zh
 * @CreateTime: 2025-04-19
 * @Description: 响应体统一处理
 * @Version: 1.0
 */
@RestControllerAdvice
@Slf4j
public class ResponseBodyHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 响应结果转换成weaverResponse
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof WeaverResponse<?>){
            return body;
        } else if (body == null){
            return WeaverResponse.success();
        } else {
            return WeaverResponse.success(body);
        }
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handleException(Exception e, HttpServletRequest request) {
        log.error("Global Exception Occured => url : {}", request.getRequestURL());
        log.error(ExceptionUtils.getStackTrace(e));
        return WeaverResponse.error(e.getMessage());
    }
}
