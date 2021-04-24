package com.crmdemo.crm.workbench.service;

import com.crmdemo.crm.utils.SqlSessionUtil;
import com.crmdemo.crm.workbench.dao.TranDao;
import com.crmdemo.crm.workbench.domain.Tran;
import com.crmdemo.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranService {
    Boolean save(Tran t, String customerName);

    Tran getTranDetail(String id);

    List<TranHistory> getTranHistoryList(String tranId);


    //创建需要的dao
    //搭建controller层
    // 修改web.xml文件（交易控制器）

    //第一个需求，点击创建按钮，跳转到添加页面（以前的形式是打开模态窗口
    //此时要过后台，取出所有者（跳转页面，发送的是一个传统请求）

}
