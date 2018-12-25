package com.minecenter.config;

import com.minecenter.exception.CustomException;
import com.minecenter.exception.CustomUnauthorizedException;
import com.minecenter.model.common.ResponseBean;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常控制处理器
 * @author chunsiyang
 * @date 2018/8/30 14:02
 */
@RestControllerAdvice
public class ExceptionAdvice {
    /**
     * 捕捉所有Shiro异常
     * @param e ShiroException
     * @return ResponseBean
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResponseBean handle401(ShiroException e) {
        return new ResponseBean(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):" + e.getMessage(), null);
    }

    /**
     * 单独捕捉Shiro(UnauthorizedException)异常
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     * @param e UnauthorizedException
     * @return ResponseBean
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseBean handle401(UnauthorizedException e) {
        return new ResponseBean(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):当前Subject没有此请求所需权限(" + e.getMessage() + ")", null);
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     * @param e UnauthenticatedException
     * @return ResponseBean
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseBean handle401(UnauthenticatedException e) {
        return new ResponseBean(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):当前Subject是匿名Subject，请先登录(This subject is anonymous.)", null);
    }

    /**
     * 捕捉UnauthorizedException自定义异常
     * @return ResponseBean
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomUnauthorizedException.class)
    public ResponseBean handle401(CustomUnauthorizedException e) {
        return new ResponseBean(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):" + e.getMessage(), null);
    }

    /**
     * 捕捉其他所有自定义异常
     * @return ResponseBean
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public ResponseBean handle(CustomException e) {
        return new ResponseBean(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    /**
     * 捕捉404异常
     * @return ResponseBean
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseBean handle(NoHandlerFoundException e) {
        return new ResponseBean(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
    }

    /**
     * 捕捉其他所有异常
     * @param request HttpServletRequest
     * @param ex Exception
     * @return ResponseBean
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseBean globalException(HttpServletRequest request, Throwable ex) {
        return new ResponseBean(this.getStatus(request).value(), ex.toString() + ": " + ex.getMessage(), null);
    }

    /**
     * 获取状态码
     * @param request HttpServletRequest
     * @return HttpStatus
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
