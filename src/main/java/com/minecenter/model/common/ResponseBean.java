package com.minecenter.model.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ResponseBean
 *
 * @author chunsiyang
 * @date 2018/8/30 11:39
 */
@ApiModel(value = "ResponseBean", description = "RESTful API 统一 Response Bean（RESTful API unify Response Bean）")
public class ResponseBean {

    /**
     * HTTP状态码
     */
    @ApiModelProperty(value = "HTTP 状态码(HTTP Response code)", required = true)
    private Integer code;

    /**
     * 返回信息
     */
    @ApiModelProperty(value = "信息(Message)", required = true)
    private String msg;

    /**
     * 返回的数据
     */
    @ApiModelProperty(value = "返回的json data(Response json data)", required = true)
    private Object data;

    public ResponseBean(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
