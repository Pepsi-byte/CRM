package com.crmdemo.crm.workbench.dao;

import com.crmdemo.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    public List<Activity> getClueActivity(String clueId) ;

    public int saveActivity(Activity activity);

    public List<Activity> getActivityListByCondition(Map<String,Object> map);

    public int getTotalCountByCondition(Map<String,Object> map);

    public int deleteActivity(String[] ids);

    public Activity getActivityById(String id);

    public int updateActivity(Activity activity);

    public Activity getDetailById(String id);

    List<Activity> getActivityWithClueNoHadClue(Map<String, String> map);

    List<Activity> getActivityByName(String name);
}
