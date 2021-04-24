package com.crmdemo.crm.workbench.service.impl;

import com.crmdemo.crm.utils.DateTimeUtil;
import com.crmdemo.crm.utils.SqlSessionUtil;
import com.crmdemo.crm.utils.UUIDUtil;
import com.crmdemo.crm.workbench.dao.*;
import com.crmdemo.crm.workbench.domain.*;
import com.crmdemo.crm.workbench.service.ClueService;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClueServiceImpl implements ClueService {
    //与线索有关的表
    private ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //与联系人有关的表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public boolean save(Clue c) {
        Boolean flag=true;
       int count=clueDao.saveClue(c);
       if(count!=1){
           flag=false;
       }
       return flag;
    }

    @Override
    public Clue getDetail(String id) {

        Clue clue=clueDao.getDetail(id);
        return clue;
    }

    @Override
    public Boolean unbund(String id) {
        Boolean flag=true;
        int count=clueActivityRelationDao.unbund(id);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Boolean setRelation(String cid, String[] aid) {
        Boolean flag=true;
        for(String Aid:aid){
            String id= UUIDUtil.getUUID();
            ClueActivityRelation activityRelation=new ClueActivityRelation();
            activityRelation.setId(id);
            activityRelation.setActivityId(Aid);
            activityRelation.setClueId(cid);

            int count=clueActivityRelationDao.setRelation(activityRelation);
            if(count!=1){
                flag=false;
            }
        }
        return flag;
    }

    @Override
    public Boolean convert(String clueId, Tran t, String createBy) {
        //由于这个转换操作设计到多张表，所以要先将每一张表要使用到的dao先声明出来
        String createTime= DateTimeUtil.getSysTime();
        Boolean flag=true;

        //(1)通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue=clueDao.getClueById(clueId);//拿到了这条交易的所有信息

        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company=clue.getCompany();
        Customer customer=customerDao.getCustomerByName(company);
        if(customer==null){
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateBy(createBy);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setDescription(clue.getDescription());
            customer.setName(company);//客户的名称就是公司的名称，客户表保存的信息是和公司相关的信息
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setOwner(clue.getOwner());
            customer.setPhone(clue.getPhone());
            customer.setWebsite(clue.getWebsite());

            int count1=customerDao.save(customer);
            if(count1!=1){
                flag=false;
            }

        }

        //(3)通过线索对象提取联系人信息，保存联系人
        Contacts con=new Contacts();

        con.setId(UUIDUtil.getUUID());
        con.setSource(clue.getSource());
        con.setOwner(clue.getOwner());
        con.setNextContactTime(clue.getNextContactTime());
        con.setMphone(clue.getMphone());
        con.setJob(clue.getJob());
        con.setFullname(clue.getFullname());
        con.setEmail(clue.getEmail());
        con.setDescription(clue.getDescription());
        con.setCustomerId(customer.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(clue.getContactSummary());
        con.setAppellation(clue.getAppellation());
        con.setAddress(clue.getAddress());
        //添加联系人
        int count2 = contactsDao.save(con);
        if(count2!=1){
            flag = false;
        }


        //--------------------------------------------------------------------------
        //经过第三步处理后，联系人的信息我们已经拥有了，将来在处理其他表的时候，如果要使用到联系人的id
        //直接使用con.getId();
        //--------------------------------------------------------------------------

        //(4) 线索备注转换到客户备注以及联系人备注
        //查询出与该线索关联的备注信息列表
        //先查询出来
        List<ClueRemark> clueRemarks=clueRemarkDao.getClueRemarkByClueId(clueId);
        //取出每一条线索的备注
        for(ClueRemark clueRemark:clueRemarks){
            //取出备注信息，将他转换到客户备注和凉席人备注中
            String noteContend=clueRemark.getNoteContent();

           //先放到客户备注信息表
            CustomerRemark customerRemark=new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setNoteContent(noteContend);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer.getId());

            int count4=customerRemarkDao.save(customerRemark);
            if(count4!=1){
                flag=false;
            }

            //将备注信息放到联系人备注信息表
            ContactsRemark contactsRemark=new ContactsRemark();
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContend);

            int count5=contactsRemarkDao.save(contactsRemark);

            if(count5!=1){
                flag=false;
            }

        }//for循环

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
        //联系人与市场活动移动存在3个元素，随机生成的id,   activityId   contactId
        //所以，我们已经知道了clueId,查询出一条线索所对应的多条的市场活动
        List<ClueActivityRelation> clueActivityRelations=clueActivityRelationDao.getListByClueId(clueId);

        for(ClueActivityRelation clueActivityRelation:clueActivityRelations){
            String activityId=clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());

            int count6=contactsActivityRelationDao.save(contactsActivityRelation);

            if(count6!=1){
                flag=false;
            }
        }//for循环结束

        /*
        如果没有创建交易，那么就是在页面中不勾选，那么在数据库表中也不会出现交易信息
         */
        //(6)如果有创建交易需求，创建一条交易
        if(t!=null) {

            /*

                t对象在controller里面已经封装好的信息如下：
                    id,money,name,expectedDate,stage,activityId,createBy,createTime

                接下来可以通过第一步生成的c对象，取出一些信息，继续完善对t对象的封装

             */

            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(con.getId());

            //添加交易
            int count6 = tranDao.save(t);
            if (count6 != 1) {
                flag = false;
            }



            //如果创建了交易，就要在交易历史记录里面新增一条交易历史记录
            TranHistory tranHistory=new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            //将创建的交易历史记录添加到交易历史记录表
            int count7=tranHistoryDao.save(tranHistory);
            if(count7!=1){
                flag=false;
            }

        }

        //先删除线索备注
        for(ClueRemark clueRemark:clueRemarks){
            int count8=clueRemarkDao.delete(clueRemark);
            if(count8!=1){
                flag=false;
            }
        }

        //在删除线索与市场活动的关系
        for(ClueActivityRelation clueActivityRelation:clueActivityRelations){
            int count9=clueActivityRelationDao.delete(clueActivityRelation);
            if(count9!=1){
                flag=false;
            }
        }


        //最后删除线索
        int count10=clueDao.delete(clueId);
        if(count10!=1){
            flag=false;
        }
            return flag;
        }


}
