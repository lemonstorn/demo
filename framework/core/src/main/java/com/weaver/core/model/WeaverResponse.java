package com.weaver.core.model;


import com.weaver.core.enums.CodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "通用返回对象")
@Data
public class WeaverResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 5668284352904314487L;

    @Schema(name = "状态(0成功1错误2未登陆3刷新4重复提交)")
    private Integer status;
    @Schema(name = "错误信息")
    private String msg;
    @Schema(name = "数据对象")
    private T data;
    @Schema(hidden = true)
    @JsonIgnore
    private transient Boolean success;
    @JsonIgnore
    private transient Boolean error;

    public WeaverResponse() {

    }

    public WeaverResponse(Integer status) {
        this.status = status;
    }

    public WeaverResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public WeaverResponse(T data) {
        this.data = data;
    }

    public WeaverResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public WeaverResponse(CodeEnum codeEnum) {
        this.status = codeEnum.getCode();
        this.msg = codeEnum.getName();
    }

    public static WeaverResponse<String> success() {
        return new WeaverResponse<>(CodeEnum.Success.getCode());
    }

    public static <E> WeaverResponse<E> success(E E) {
        WeaverResponse<E> ajaxResult = new WeaverResponse<>(E);
        ajaxResult.setStatus(CodeEnum.Success.getCode());
        return ajaxResult;
    }

    public static <E> WeaverResponse<E> success(String message) {
        WeaverResponse<E> ajaxResult = new WeaverResponse<>();
        ajaxResult.setStatus(CodeEnum.Success.getCode());
        ajaxResult.setMsg(message);
        return ajaxResult;
    }

    public static <E> WeaverResponse<E> success(E E, String message) {
        WeaverResponse<E> ajaxResult = new WeaverResponse<>(E);
        ajaxResult.setMsg(message);
        ajaxResult.setStatus(CodeEnum.Success.getCode());
        return ajaxResult;
    }

    public static <E> WeaverResponse<E> error(int code, String msg) {
        WeaverResponse<E> ajaxResult = new WeaverResponse<>(code);
        ajaxResult.setMsg(msg);
        return ajaxResult;
    }

    public static <E> WeaverResponse<E> error(String msg) {
        WeaverResponse<E> ajaxResult = new WeaverResponse<>(CodeEnum.Fail.getCode());
        ajaxResult.setMsg(msg);
        return ajaxResult;
    }

    public static <E> WeaverResponse<E> error(CodeEnum code) {
        return new WeaverResponse<>(code.getCode(), code.getName());
    }

    public static <E> WeaverResponse<E> error(E E, String msg) {
        WeaverResponse<E> ajaxResult = new WeaverResponse<>(E);
        ajaxResult.setStatus(CodeEnum.Fail.getCode());
        ajaxResult.setMsg(msg);
        ajaxResult.setData(E);
        return ajaxResult;
    }

    public static WeaverResponse<String> noLogin() {
        return new WeaverResponse<>(CodeEnum.USER_NOT_LOGIN);
    }

    public static WeaverResponse<String> singleLogin() {
        return new WeaverResponse<>(CodeEnum.USER_ACCOUNT_USE_BY_OTHERS);
    }

    public static WeaverResponse<String> notFound() {
        WeaverResponse<String> ajaxResult = new WeaverResponse<>(CodeEnum.Fail.getCode());
        ajaxResult.setMsg("资源未找到");
        return ajaxResult;
    }

    public static WeaverResponse<String> isRepeat() {
        return new WeaverResponse<>(CodeEnum.REPEAT_SUBMIT);
    }

    public boolean isSuccess() {
        if (this.status == null) {
            this.success = false;
        }
        this.success = CodeEnum.Success.getCode().equals(this.status);
        return success;
    }

    public boolean isError() {
        this.error = CodeEnum.Success.getCode().equals(this.status);
        return error;
    }

}
