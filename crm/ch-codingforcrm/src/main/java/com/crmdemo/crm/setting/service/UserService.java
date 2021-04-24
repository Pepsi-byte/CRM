package com.crmdemo.crm.setting.service;

import com.crmdemo.crm.exception.LoginException;
import com.crmdemo.crm.setting.domain.User;

import java.util.List;

public interface UserService {

    public User login(String loginUserName, String loginPwd, String ip) throws LoginException;
    public List<User> getUserList();
}
