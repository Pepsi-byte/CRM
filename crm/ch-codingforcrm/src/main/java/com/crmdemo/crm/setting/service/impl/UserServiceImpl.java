package com.crmdemo.crm.setting.service.impl;

import com.crmdemo.crm.exception.LoginException;
import com.crmdemo.crm.setting.dao.UserDao;
import com.crmdemo.crm.setting.domain.User;
import com.crmdemo.crm.setting.service.UserService;
import com.crmdemo.crm.utils.DateTimeUtil;
import com.crmdemo.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

     private UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

     @Override
     public User login(String loginUserName, String loginPwd, String ip) throws LoginException {
          //由于传入dao层，要调用dao的方法执行，dao在mapper文件中，传入多个参数，要将参数分装成map
          Map<String,Object> map=new HashMap<String,Object>();
          map.put("loginUserName",loginUserName);
          map.put("loginPwd",loginPwd);
          //map.put("ip",ip);

         User user=userDao.login(map);
         if(user==null){
              //一旦抛出异常，程序就不会继续往下面执行
              throw new LoginException("账号或密码错误");
         }

         //账户和密码如果到了这里，就说明账号和密码正确，继续验证其他信息

          //验证失效时间
          String expireTime=user.getExpireTime();//失效时间
          String currentTime= DateTimeUtil.getSysTime();
          if(expireTime.compareTo(currentTime)<0){
               throw new LoginException("当前账户已失效");
          }

          //验证ip地址
          String allowIps=user.getAllowIps();
          if(!allowIps.contains(ip)){
               throw new LoginException("ip地址受到了限制，请联系管理员！");
          }

          //验证锁定状态
          String lockState=user.getLockState();
          if("0".equals(lockState)){
               throw new LoginException("当前账号已锁定");
          }

          //全部都验证成功的话，就返回user
          return user;
     }



    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }

}
