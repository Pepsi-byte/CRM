package com.crmdemo.crm.workbench.dao;

import com.crmdemo.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    public int getCount(String[] ids);

    public int deleteRemark(String[] ids);

    public List<ActivityRemark> getRemarkById(String id);

    public int deleteRemarkById(String id);

    public int saveRemark(ActivityRemark ar);

    public int updateRemark(ActivityRemark activityRemark);
}
