package com.crmdemo.crm.workbench.service.impl;

import com.crmdemo.crm.utils.DateTimeUtil;
import com.crmdemo.crm.utils.SqlSessionUtil;
import com.crmdemo.crm.utils.UUIDUtil;
import com.crmdemo.crm.workbench.dao.CustomerDao;
import com.crmdemo.crm.workbench.dao.TranDao;
import com.crmdemo.crm.workbench.dao.TranHistoryDao;
import com.crmdemo.crm.workbench.domain.Customer;
import com.crmdemo.crm.workbench.domain.Tran;
import com.crmdemo.crm.workbench.domain.TranHistory;
import com.crmdemo.crm.workbench.service.TranService;

import java.util.Date;
import java.util.List;

public class TranServiceImpl implements TranService {
    //创建需要的dao
    //搭建controller层
    // 修改web.xml文件（交易控制器）

    //第一个需求，点击创建按钮，跳转到添加页面（以前的形式是打开模态窗口
    //此时要过后台，取出所有者（跳转页面，发送的是一个传统请求）

    private TranDao trandao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    //添加关于客户相关的表
    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public Boolean save(Tran t, String customerName) {
        /**
         * 首先，根据穿过来的客户名称，此时传递过来的是一个公司的名称
         * 根据公司的名称精确查询，查询到这个公司的id,此时，如果可以查询到，就说明这个客户存在
         *
         * 如果查询的结果为空，就说明用户不存在，需要新建一个客户
         *
         * 新建客户完成之后，将信息添加到交易表中
         *
         * 添加成功的话，在为这一条交易创建一条历史交易记录
         */

        Boolean flag=true;

       Customer customer=customerDao.getCustomerByName(customerName);

       //查询结果为空，创建一条客户信息
       if(customer==null){
           customer=new Customer();
           customer.setId(UUIDUtil.getUUID());
           customer.setOwner(t.getOwner());
           customer.setContactSummary(t.getContactSummary());
           customer.setNextContactTime(t.getNextContactTime());
           customer.setName(customerName);
           customer.setCreateTime(DateTimeUtil.getSysTime());
           customer.setCreateBy(t.getCreateBy());
           customer.setDescription(t.getDescription());

//           将创建好的信息添加到客户表中
           int count1=customerDao.save(customer);
           if(count1!=1){
               flag=false;
           }
       }


       //经过了上述的步骤之后，客户表中的信息就一定存在
        //在这里就可以将客户表的id添加到交易表中

        t.setCustomerId(customer.getId());
       //此时：交易信息表中的信息就补全了，将这条交易添加到交易信息表中
        int count2=trandao.save(t);
        if(count2!=1){
            flag=false;
        }

        //创建交易成功，就为交易历史表新创建一条记录
        TranHistory tranHistory=new TranHistory();
        tranHistory.setTranId(t.getId());
        tranHistory.setStage(t.getStage());
        tranHistory.setMoney(t.getMoney());
        tranHistory.setExpectedDate(t.getExpectedDate());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(t.getCreateBy());
        tranHistory.setId(UUIDUtil.getUUID());


        int count3=tranHistoryDao.save(tranHistory);
        if(count3!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public Tran getTranDetail(String id) {
        //获取交易的详细信息
        Tran tran=trandao.getTranDetail(id);
        return tran;
    }

    @Override
    public List<TranHistory> getTranHistoryList(String tranId) {
        List<TranHistory> tranHistoryList=tranHistoryDao.getTranHistoryList(tranId);
        return tranHistoryList;
    }
}
