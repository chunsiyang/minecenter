package com.minecenter.model.entry;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@PropertySource("classpath:config.properties")
@ApiModel(value = "User", description = "用户")
@Table(name = "sys_user")
public class User extends BaseBean implements Serializable {

    @Id
    @KeySql(sql = "select uuid()",order = ORDER.BEFORE)
    @ApiModelProperty(hidden = true)
    private String id;

    @NotBlank
    @ApiModelProperty(value = "用户名称(User Account)", required = true)
    private String account;

    @NotBlank
    @ApiModelProperty(value = "用户密码(User password)", required = true)
    private String password;

    @ApiModelProperty(hidden = true)
    private String username;

    @ApiModelProperty(hidden = true)
    private Date regTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }
}