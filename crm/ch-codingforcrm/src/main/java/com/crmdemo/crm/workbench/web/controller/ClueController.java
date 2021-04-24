package com.crmdemo.crm.workbench.web.controller;

import com.crmdemo.crm.setting.domain.User;
import com.crmdemo.crm.setting.service.UserService;
import com.crmdemo.crm.setting.service.impl.UserServiceImpl;
import com.crmdemo.crm.utils.DateTimeUtil;
import com.crmdemo.crm.utils.PrintJson;
import com.crmdemo.crm.utils.ServiceFactory;
import com.crmdemo.crm.utils.UUIDUtil;
import com.crmdemo.crm.workbench.domain.Activity;
import com.crmdemo.crm.workbench.domain.Clue;
import com.crmdemo.crm.workbench.domain.Tran;
import com.crmdemo.crm.workbench.service.ActivityService;
import com.crmdemo.crm.workbench.service.ClueService;
import com.crmdemo.crm.workbench.service.impl.ActivityServiceImpl;
import com.crmdemo.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ClueController")
public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到线索控制器！");
        String path=req.getServletPath();
        if("/workbench/clue/getUserList.do".equals(path)){
            System.out.println("获取用户的所有信息");
            getUserList(req,resp);


        }else if("/workbench/clue/saveClue.do".equals(path)){
            System.out.println("保存线索信息");
            saveClue(req,resp);
        }else if("/workbench/clue/detail.do".equals(path)){
            System.out.println("查看线索的详细信息");
            getDetail(req,resp);
        }else if("/workbench/clue/getClueActivity.do".equals(path)){
            System.out.println("根据线索id查询关联的市场活动列表");
            getClueActivity(req,resp);
        }else if("/workbench/clue/unbund.do".equals(path)){
            System.out.println("解除线索与市场活动的关联");
            unbund(req,resp);
        }else if("/workbench/clue/getActivityWithClueNoHadClue.do".equals(path)){
            System.out.println("查询关联市场活动列表，除了以前关联过的数据");
            getActivityWithClueNoHadClue(req,resp);
        }else if("/workbench/clue/setRelation.do".equals(path)){
            System.out.println("根剧cid  aid冠梁线索与市场活动");
            setRelation(req, resp);
        }else if("/workbench/clue/getActivityByName.do".equals(path)){
            System.out.println("转货线索搜索市场活动");
            getActivityByName(req,resp);
        }else if("/workbench/clue/convert.do".equals(path)){
            System.out.println("执行线索转换的操作！");
            convert(req,resp);
        }



    }

    /**
     * 当潜在的客户与我们所在的公司签订了合同之后，潜在的用户就成为了我们的用户，所以：
     * 将潜在客户的公司转换成客户与联系人两个模块，并且将客户从潜在客户中删除
     * @param request
     * @param response
     */
    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接收参数
        String clueId=request.getParameter("clueId");
        //接收是否需要创建交易的标记
        String flag=request.getParameter("flag");
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        Tran t=null;
        if("a".equals(flag)){
            //就是说明需要创建交易信息，在这里接收表单中的参数
            t = new Tran();

            //接收交易表单中的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();



            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);

        }

        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        /*
        传递的参数必须要是clueId:只有知道了线索的id,才能知道我们要为那一条线索做转换
        传递的参数也必须要有t,因为在线索转换的过程中，有可能会临时的区创建一笔交易（但是接收到的t也有可能是为一个null）
         */

        /**
         * flag1:返回true就代表转换成功，返回false就代表转换失败
         *
         */
        Boolean flag1=clueService.convert(clueId,t,createBy);
        //转换成功，就跳回原来的主页，用重定向
        response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");

    }

    private void getActivityByName(HttpServletRequest req, HttpServletResponse resp) {
        String name=req.getParameter("name");

        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activityList=activityService.getActivityByName(name);
        PrintJson.printJsonObj(resp,activityList);
    }

    private void setRelation(HttpServletRequest req, HttpServletResponse resp) {
        String cid=req.getParameter("cid");
        String aid[]=req.getParameterValues("aid");

        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag=clueService.setRelation(cid,aid);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getActivityWithClueNoHadClue(HttpServletRequest req, HttpServletResponse resp) {
        String name=req.getParameter("activityName");
        String id=req.getParameter("clueId");

        Map<String,String> map=new HashMap<>();
        map.put("name",name);
        map.put("id",id);

        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
       List<Activity> activityList=activityService.getActivityWithClueNoHadClue(map);
        System.out.println(activityList);
       PrintJson.printJsonObj(resp,activityList);
    }

    private void unbund(HttpServletRequest req, HttpServletResponse resp) {
        String id=req.getParameter("id");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag=clueService.unbund(id);

        PrintJson.printJsonFlag(resp,flag);
    }

    private void getClueActivity(HttpServletRequest req, HttpServletResponse resp) {

        String clueId=req.getParameter("clueId");//传过来的是线索表中的id,而线索表中的id也会存在于线索活动关联表中
        System.out.println(clueId);
        //操作的是activity表
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activity=activityService.getClueActivity(clueId);
        System.out.println("==============="+activity);

        PrintJson.printJsonObj(resp,activity);

    }

    private void getDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id=req.getParameter("id");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue=clueService.getDetail(id);
        System.out.println(clue);
        req.setAttribute("c",clue);

        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {


        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setContactSummary(contactSummary);
        c.setCompany(company);
        c.setAppellation(appellation);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest req,HttpServletResponse resp) {
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList=userService.getUserList();
        PrintJson.printJsonObj(resp,userList);
    }


}
