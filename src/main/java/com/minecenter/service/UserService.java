package com.minecenter.service;

import com.minecenter.mapper.UserMapper;
import com.minecenter.model.entry.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserMapper userMapper;

    @Autowired
    private UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User selectOne(User user) {
        return userMapper.selectOne(user);
    }

}
