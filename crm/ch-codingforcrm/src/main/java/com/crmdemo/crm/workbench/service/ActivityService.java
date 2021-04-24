package com.crmdemo.crm.workbench.service;

import com.crmdemo.crm.vo.PaginationVo;
import com.crmdemo.crm.workbench.domain.Activity;
import com.crmdemo.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    public Boolean saveActivity(Activity activity);

    public PaginationVo<Activity> getPageList(Map<String,Object> map);

    public Boolean deleteAcivity(String[] ids);

    public Map<String,Object> getUserListAndActivity(String id);

    public Boolean updateActivity(Activity activity);

    public Activity getDetail(String id);

    public List<ActivityRemark> getActivityRemarrk(String id);

    public Boolean deleteRemarkById(String id);

    public Boolean saveRemark(ActivityRemark ar);

    public Boolean updateRemark(ActivityRemark ar);

    List<Activity> getClueActivity(String clueId);

    List<Activity> getActivityWithClueNoHadClue(Map<String, String> map);

    List<Activity> getActivityByName(String name);
}
