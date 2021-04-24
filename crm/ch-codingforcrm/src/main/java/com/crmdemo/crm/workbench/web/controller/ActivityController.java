package com.crmdemo.crm.workbench.web.controller;

import com.crmdemo.crm.setting.domain.User;
import com.crmdemo.crm.setting.service.UserService;
import com.crmdemo.crm.setting.service.impl.UserServiceImpl;
import com.crmdemo.crm.utils.DateTimeUtil;
import com.crmdemo.crm.utils.PrintJson;
import com.crmdemo.crm.utils.ServiceFactory;
import com.crmdemo.crm.utils.UUIDUtil;
import com.crmdemo.crm.vo.PaginationVo;
import com.crmdemo.crm.workbench.domain.Activity;
import com.crmdemo.crm.workbench.domain.ActivityRemark;
import com.crmdemo.crm.workbench.service.ActivityService;
import com.crmdemo.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ActivityController")
public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器！");
        String path=req.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){
            System.out.println("获取用户信息列表");
            getUserList(req,resp);

        }else if("/workbench/activity/saveActivity.do".equals(path)){
            System.out.println("添加市场活动信息！");
            saveActivity(req,resp);

        }else if("/workbench/activity/getActivityList.do".equals(path)){
            System.out.println("进入到查询市场活动信息的列表的操作（结合条件查询和分页查询）");
            getPageList(req,resp);
        }else if("/workbench/activity/deleteActivityList.do".equals(path)){
            System.out.println("进入到了删除市场活动的操作");
            delectActivity(req,resp);
        }else if("/workbench/activity/editActivity.do".equals(path)){
            System.out.println("将数据库中的市场活动信息铺到模态窗口上");
            editActivity(req,resp);
        }else if("/workbench/activity/updateActivity.do".equals(path)){
            System.out.println("进入到修改市场活动的操作");
            updateActivity(req,resp);
        }else if("/workbench/activity/activityDetail.do".equals(path)){
            System.out.println("进入到查看详细消息的操作中！");
            activityDetail(req,resp);
        }else if("/workbench/activity/getRemarkActivity.do".equals(path)){
            System.out.println("获取到市场活动的备注信息！");
            getRemarkActivity(req,resp);
        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            System.out.println("进入到删除一条市场活动的备注的操作");
            deleteRemark(req,resp);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            System.out.println("添加市场活动的备注信息");
            saveRemark(req,resp);
        }else if("/workbench/activity/updateRemark.do".equals(path)){
            System.out.println("更新备注信息");
            updateRemark(req,resp);
        }
    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {
        String id=req.getParameter("id");
        String notrContent=req.getParameter("noteContent");
        String editTime=DateTimeUtil.getSysTime();
        String editBy=((User)req.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark activityRemark=new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(notrContent);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditFlag(editFlag);

        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

       Boolean flag=activityService.updateRemark(activityRemark);

       Map<String,Object> map=new HashMap<>();
       map.put("success",flag);
       map.put("ar",activityRemark);

       PrintJson.printJsonObj(resp,map);

    }

    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {
        String noteContent=req.getParameter("noteContent");
        String activityId=req.getParameter("activityId");
        String id=UUIDUtil.getUUID();
        String createTime=DateTimeUtil.getSysTime();
        String createBy=((User)req.getSession().getAttribute("user")).getName();//或去当前在登录对象的姓名
        String editFlag="0";//新增一条数据，初始的结果一定是0

        ActivityRemark activityRemark=new ActivityRemark();

        activityRemark.setActivityId(activityId);
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateBy(createBy);
        activityRemark.setCreateTime(createTime);
        activityRemark.setEditFlag(editFlag);

        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Boolean flag=activityService.saveRemark(activityRemark);
       // System.out.println(flag);

        //返回两种数据，封装成map
        Map<String,Object> map=new HashMap<>();
        map.put("ar",activityRemark);
        map.put("success",flag);

        PrintJson.printJsonObj(resp,map);
    }

    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp) {

        String id=req.getParameter("id");//获取需要删除的备注信息的id
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=activityService.deleteRemarkById(id);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getRemarkActivity(HttpServletRequest req, HttpServletResponse resp) {

        String id=req.getParameter("activityId");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> activityRemarkList=activityService.getActivityRemarrk(id);
        for(ActivityRemark activityRemark:activityRemarkList){
            System.out.println("----------------"+activityRemark);
        }
        PrintJson.printJsonObj(resp,activityRemarkList);

    }

    private void activityDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取id,要查看那一条市场活动的详细信息
        String id=req.getParameter("id");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity=activityService.getDetail(id);
        req.setAttribute("activity",activity);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);
    }

    private void updateActivity(HttpServletRequest request, HttpServletResponse response) {
        //获取ajax请求的参数
        String id=request.getParameter("id");
        String owner =request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost =request.getParameter("cost");
        String description=request.getParameter("description");
        String editTime= DateTimeUtil.getSysTime();//获取当前系统时间
        String editBy=((User)request.getSession().getAttribute("user")).getName();//过去当前正在登陆系统的人的名字

//        System.out.println("！！！！！"+editTime);
//        System.out.println("7777777777777"+editBy);
        //获取到了参数之后，需要将值传入service层进行处理，但是参数太多了，将其进行封装
        Activity activity=new Activity();

        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(editTime);
        activity.setCreateBy(editBy);
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);

        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=activityService.updateActivity(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void editActivity(HttpServletRequest req, HttpServletResponse resp) {

        String id=req.getParameter("id");
//        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Map<String,Object> map=activityService.getUserListAndActivity(id);
        PrintJson.printJsonObj(resp,map);

    }


    public void getUserList(HttpServletRequest request,HttpServletResponse response){

        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> users=userService.getUserList();

        //将查询到的结果转成json类型传回前端显示
        PrintJson.printJsonObj(response,users);
    }


    //添加市场活动信息的操作
    public void  saveActivity(HttpServletRequest request,HttpServletResponse response){
        //获取ajax请求的参数
        String id= UUIDUtil.getUUID();
        String owner =request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost =request.getParameter("cost");
        String description=request.getParameter("description");
        String createTime= DateTimeUtil.getSysTime();//获取当前系统时间
        String createBy=((User)request.getSession().getAttribute("user")).getName();//过去当前正在登陆系统的人的名字

        //获取到了参数之后，需要将值传入service层进行处理，但是参数太多了，将其进行封装
        Activity activity=new Activity();

        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        //使用动态代理，创建service对象
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /**
        * @Description:
        * @Param: [request, response]
        * @return: 返回的应该是添加成功的结果true/false
        * @Author: z
        * @Date: 2021/4/17
        */

       Boolean flag=activityService.saveActivity(activity);
//        System.out.println(flag);
       //将返回的结果封装成json对象，返回到前端显示
        PrintJson.printJsonFlag(response,flag);
    }



    public void getPageList(HttpServletRequest request,HttpServletResponse response){

        String name=request.getParameter("search-name");
        String owner=request.getParameter("search-owner");
        String startDate=request.getParameter("search-startDate");
        String endDate=request.getParameter("search-endDate");
        String pageNoStr=request.getParameter("pageNo");
        int pageNo=Integer.valueOf(pageNoStr);//表示的是第几页
        String pageSizeStr=request.getParameter("pageSize");//每页展现的记录数
        int pageSize=Integer.valueOf(pageSizeStr);

        //计算出需要略过的条数
        int skipNum=(pageNo-1)*pageSize;

        //这些数据最终都需要传递进去dao层，最后拿给sql语句进行查询使用，所以要封装成map
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipNum",skipNum);
        map.put("pageSize",pageSize);

        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        /*
        注意： activityService.getPageList(map)
        这条语句的返回值是什么的，看前端需要的是什么：
        前端需要：
                市场信息的活动列表
                查询的总的记录条数

               使用map或者vo:由于分页在之后的很多情况下都要使用，所以封装成vo
               要做成一个通用的，就要封装成一个泛型

               paginationVo<T>
                private int total;
                private List<T> dataList;

                paginationVo<Activity> pagination=new Pagination<>();
                         */
        PaginationVo<Activity> vo=activityService.getPageList(map);
        PrintJson.printJsonObj(response,vo);

    }


    public void delectActivity(HttpServletRequest request,HttpServletResponse response){

        String ids[]=request.getParameterValues("id");

        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=activityService.deleteAcivity(ids);
        PrintJson.printJsonFlag(response,flag);
    }









































































}
