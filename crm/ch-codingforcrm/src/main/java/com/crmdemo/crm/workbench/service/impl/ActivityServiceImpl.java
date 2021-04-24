package com.crmdemo.crm.workbench.service.impl;

import com.crmdemo.crm.setting.dao.UserDao;
import com.crmdemo.crm.setting.domain.User;
import com.crmdemo.crm.utils.SqlSessionUtil;
import com.crmdemo.crm.vo.PaginationVo;
import com.crmdemo.crm.workbench.dao.ActivityDao;
import com.crmdemo.crm.workbench.dao.ActivityRemarkDao;
import com.crmdemo.crm.workbench.domain.Activity;
import com.crmdemo.crm.workbench.domain.ActivityRemark;
import com.crmdemo.crm.workbench.service.ActivityService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public Activity getDetail(String id) {
        Activity activity=activityDao.getDetailById(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getActivityRemarrk(String id) {
        List<ActivityRemark> activityRemarkList= activityRemarkDao.getRemarkById(id);
        return activityRemarkList;
    }

    @Override
    public Boolean deleteRemarkById(String id) {
        Boolean flag=true;
        int count=activityRemarkDao.deleteRemarkById(id);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Boolean saveRemark(ActivityRemark ar) {
        Boolean flag=true;
        int count=activityRemarkDao.saveRemark(ar);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Boolean updateRemark(ActivityRemark ar) {
        Boolean flag=true;
        int count=activityRemarkDao.updateRemark(ar);

        if(count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public List<Activity> getClueActivity(String clueId) {
        List<Activity> activityList=new ArrayList<>();
        activityList=activityDao.getClueActivity(clueId);
        System.out.println(activityList);
        return activityList;
    }

    @Override
    public List<Activity> getActivityWithClueNoHadClue(Map<String, String> map) {
        List<Activity> activityList=activityDao.getActivityWithClueNoHadClue(map);
        return activityList;
    }

    @Override
    public List<Activity> getActivityByName(String name) {
        List<Activity> activityList=activityDao.getActivityByName(name);
        return activityList;
    }

    @Override
    public Boolean saveActivity(Activity activity) {
        //调用dao层返回的结果应该是执行这一项操作影响的行数，这里是添加操作，如果执行成功，影响的条数就是一行
        int count=activityDao.saveActivity(activity);
        Boolean flag=true;
        //返回的值是boolean类型的，所以可以进行判断
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public PaginationVo<Activity> getPageList(Map<String,Object> map) {
        //获取市场信息列表
        List<Activity> activityList=activityDao.getActivityListByCondition(map);

        //获取分页需要的总记录数
        int totalCount=activityDao.getTotalCountByCondition(map);

        //封装成vo返回前端显示
        PaginationVo<Activity> vo=new PaginationVo<Activity>();
        vo.setTotal(totalCount);
        vo.setDataList(activityList);
        return vo;
    }

    @Override
    public Boolean deleteAcivity(String[] ids) {
        Boolean flag=true;

        //由于活动列表关联了活动备注列表的信息，并且一个活动有多条备注信息，是一对多关系
        //所以要先删除活动列表中的备注信息，在删除活动列表信息

        //先查询出要删除的活动的备注有几条
        int count1=activityRemarkDao.getCount(ids);
        System.out.println("================="+count1);

        //删除活动列表的备注信息,返回影响的行数
        int count2=activityRemarkDao.deleteRemark(ids);
        System.out.println("=================="+count2);
        if(count1!=count2){
            flag=false;
        }

        //删除活动
        int count3=activityDao.deleteActivity(ids);
        System.out.println("+++++++++++++++"+count3);
        System.out.println("+++++++++++++++"+ids.length);
        if(count3!=ids.length){
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        //获取ulist
        List<User> userList=userDao.getUserList();
        //获取a
        Activity activity=activityDao.getActivityById(id);
        //将ulist和a一起放到map
          Map<String,Object> map=new HashMap<String,Object>();
          map.put("uList",userList);
          map.put("a",activity);

        //返回map

        return map;
    }

    @Override
    public Boolean updateActivity(Activity activity) {
        Boolean flag=true;
        int count=activityDao.updateActivity(activity);
        if(count!=1){
            flag=false;
        }
        return flag;
    }
}
