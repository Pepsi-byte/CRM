package com.crmdemo.crm.workbench.web.controller;

import com.crmdemo.crm.setting.domain.User;
import com.crmdemo.crm.setting.service.UserService;
import com.crmdemo.crm.setting.service.impl.UserServiceImpl;
import com.crmdemo.crm.utils.DateTimeUtil;
import com.crmdemo.crm.utils.PrintJson;
import com.crmdemo.crm.utils.ServiceFactory;
import com.crmdemo.crm.utils.UUIDUtil;
import com.crmdemo.crm.workbench.domain.Customer;
import com.crmdemo.crm.workbench.domain.Tran;
import com.crmdemo.crm.workbench.domain.TranHistory;
import com.crmdemo.crm.workbench.service.CustomerService;
import com.crmdemo.crm.workbench.service.TranService;
import com.crmdemo.crm.workbench.service.impl.CustomerServiceImpl;
import com.crmdemo.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TransactionController")
public class TransactionController extends HttpServlet {

    //如果一直不用，就会占内存
//    Map<String,String> pmap= (Map<String, String>) this.getServletContext().getAttribute("pmap");

    //交易活动控制器
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到交易活动控制器！");
        String path=req.getServletPath();
        if("/workbench/tran/add.do".equals(path)){
            System.out.println("交易活动：获取所有者！");
            add(req,resp);
        }else if("/workbench/tran/getCustomerName.do".equals(path)){
            System.out.println("取得客户名称列表（按照客户名称进行模糊查询）");
            /*
            获取与客户有关的列表
            所以需要用到的是与客户相关的业务，创建客户相关的业务
             */
            getCustomerName(req,resp);
        }else if("/workbench/tran/save.do".equals(path)){
            System.out.println("新增一条交易信息！");
            save(req,resp);
        }else if("/workbench/tran/detail.do".equals(path)){
            System.out.println("跳转到交易的详情信息页！");
            detail(req,resp);
        }else if("/workbench/tran/getTranHistoryList.do".equals(path)){
            System.out.println("获取当前交易的历史记录！");
            getTranHistoryList(req,resp);
        }
        
    }

    private void getTranHistoryList(HttpServletRequest req, HttpServletResponse resp) {
        //获取交易id,根据交易id去查询交易的历史记录
        String tranId=req.getParameter("tranId");

        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> tranHistoryList=tranService.getTranHistoryList(tranId);

        Map<String,String> pmap= (Map<String, String>) this.getServletContext().getAttribute("pmap");
        for(TranHistory tranHistory:tranHistoryList){
            String stage=tranHistory.getStage();
            String possiblity=pmap.get(stage);
            tranHistory.setPossibility(possiblity);

        }
//        遍历返回来的每一个tranHistory,拿到stage,在通过stage获取possibility
        PrintJson.printJsonObj(resp,tranHistoryList);





    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收请求发送过来的前台id
        String id=req.getParameter("id");
        //通过id查询交易信息
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        //返回的是查询到的这一条交易的详细信息
        Tran tran=tranService.getTranDetail(id);
        //将数据待会前端的页面显示

        String stage= tran.getStage();
        Map<String,String> pmap= (Map<String, String>)this.getServletContext().getAttribute("pmap");
        String possibility=pmap.get(stage);

        tran.setPossibility(possibility);
        req.setAttribute("t",tran);
        //跳转到详细信息页
        req.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(req,resp);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //接收表单中的参数
        String owner=req.getParameter("owner");
        String cost=req.getParameter("cost");
        String name=req.getParameter("name");
        String expectedDate=req.getParameter("expectedDate");
        String customerName=req.getParameter("customerName");
        String stage=req.getParameter("stage");
        String transactionType=req.getParameter("transactionType");
   //     String possibility=req.getParameter("possibility");
        String sources=req.getParameter("sources");
//        String activitySrc=req.getParameter("activitySrc");
//        String contactName=req.getParameter("contactName");

        //下面的两个id是隐藏域
        String activityId=req.getParameter("activityId");

        String contactsId=req.getParameter("contactsId");
        String description=req.getParameter("description");
        String contactSummary=req.getParameter("contactSummary");
        String nextContactTime=req.getParameter("nextContactTime");

        String id= UUIDUtil.getUUID();
        String createTime= DateTimeUtil.getSysTime();
        String createBy=((User)req.getSession().getAttribute("user")).getName();

        Tran t=new Tran();

        t.setId(id);
        t.setOwner(owner);
        t.setMoney(cost);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(transactionType);
        t.setSource(sources);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);


        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());

        Boolean flag=tranService.save(t,customerName);
        if(flag){

            //如果添加交易成功，跳转到列表页
            resp.sendRedirect(req.getContextPath() + "/workbench/transaction/index.jsp");
        }


    }

    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp) {
        //通过名称查询
        String name=req.getParameter("name");

        //需要用到的是客户的
        CustomerService customerService= (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        //从后台如果查询的话，查询到的是一个一个的客户，客户中存放的是公司的信息
        List<String> nameList=customerService.getCustomerName(name);
        PrintJson.printJsonObj(resp,nameList);

    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //通过后台，获取用户的列表，然后将用户信息发送给前台显示，前台通过jstl遍历，得到用户名
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> users=userService.getUserList();//获取所有的用户的列表

        req.setAttribute("users",users);
        req.getRequestDispatcher("/workbench/transaction/save.jsp").forward(req,resp);
    }
}
