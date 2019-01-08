package com.minecenter.api;

import com.minecenter.model.common.ResponseBean;
import com.minecenter.model.entry.User;
import com.minecenter.service.UserService;
import com.minecenter.util.AuthorizationUtil;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * UserController
 *
 * @author Wang926454
 * @date 2018/8/29 15:45
 */
@RestController
@RequestMapping("/user")
@PropertySource("classpath:config.properties")
@Api("用户相关(User Relevant)")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 测试登录
     * @return ResponseBean
     * @author chunsiyang
     * @date 2018/12/22 16:18
     */
    @ApiOperation(value="验证登录(Verify Login)", notes="验证当前token是否登录(Verify that the current token is logged in)")
    @GetMapping("/article")
    @RequiresAuthentication
    public ResponseBean article() {
        return new ResponseBean(HttpStatus.OK.value(), "您已经登录了(You are already logged in)", null);
    }

    /**
     * 登录授权
     *
     * @param user                user
     * @param httpServletResponse httpServletResponse
     * @return ResponseBean
     * @author chunsiyang
     * @date 2018/8/30 16:21
     */
    @ApiOperation(value="用户登录(User login)", notes="提供用户名密码进行登录(Provide username password for login)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "登录成功(Login Success)")})
    @PostMapping("/login")
    public ResponseBean login(
            @ApiParam(required = true, name = "user", value = "用户信息json数据(User information JSON data)")
            @RequestBody User user,
            HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Authorization", AuthorizationUtil.getBearerToken(userService.login(user)));
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        return new ResponseBean(HttpStatus.OK.value(), "登录成功(Login Success.)", null);
    }

    /**
     * 更新用户
     * @param user user
     * @return java.util.Map<java.lang.String.java.lang.Object>
     * @author chunsiyang
     * @date 2018/8/30 10:42
     */
    @ApiOperation(value="更新用户密码(update user password)", notes="更新用户密码(update user password)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "更新成功(update Success)"),
            @ApiResponse(code = 500, message = "更新失败(update Fail)")
    })
    @PutMapping
    @RequiresPermissions(value = {"user:edit"})
    public ResponseBean update(
            @ApiParam(required = true, name = "user", value = "用户信息json数据(User information JSON data)")
            @RequestBody User user) {
        userService.update(user);
        return new ResponseBean(HttpStatus.OK.value(), "更新成功（update success）", null);
    }

    /**
     * 用户主动注销
     *
     * @author chunsiyang
     * @date 2018/12/23 16:21
     */
    @ApiOperation(value="注销(logout)", notes="用户主动注销登录(Users logout)")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "注销成功(logout Success)")})
    @GetMapping("/logout")
    @RequiresAuthentication
    public ResponseBean logout() {
        Subject subject = SecurityUtils.getSubject();
        String token = subject.getPrincipal().toString();
        userService.logout(token);
        return new ResponseBean(HttpStatus.OK.value(), "注销成功（logOut success）",null);
    }
}