package com.crmdemo.crm.setting.dao;

import com.crmdemo.crm.setting.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    public User login(Map<String,Object> map);

    public List<User> getUserList();
}
