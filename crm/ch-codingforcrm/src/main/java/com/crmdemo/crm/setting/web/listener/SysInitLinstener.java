package com.crmdemo.crm.setting.web.listener;

import com.crmdemo.crm.setting.domain.DicValue;
import com.crmdemo.crm.setting.service.DicService;
import com.crmdemo.crm.setting.service.impl.DicServiceImpl;
import com.crmdemo.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

//监听上下文域对象的创建，监听哪一个域，就实现那个接口
//只要服务器一启动就自动的创建SysInitLinstener对象
//也会去执行contextInitialized方法



public class SysInitLinstener implements ServletContextListener {


    /**
     * 该方法是用来创建上下文域对象的，当服务器启动，上下文对象就会被创建
     * 对象创建完之后，就立刻执行该方法
     * 所以可以在这个方法中：去数据库中拿到需要的数据字典，保存到application上下文域对象中
     *
     * @param sce：该参数可以取得需要监听的对象application
     *           我们需要监听什么对象，就能通过sce这个参数获取到
                    如果需要监听的是request对象，那么实现的技术口就是ServlerRequestListener

     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        System.out.println("上下文域对象创建了");

        System.out.println("服务器缓存处理数据字典开始");
        //获取application上下文域对象
        ServletContext application=sce.getServletContext();

        /**
         *   从数据库中获取值，存入到数据字典
         *   对于数据字典中的数据，必须按照类型来进行保存typeCode
         *
         *   SELECT * FROM tbl_dic_value WHERE typeCode='appellation'
         *   List<DicValue> dicList=执行sql语句   5条
         *    SELECT * FROM tbl_dic_value WHERE typeCode='clueState'
         *    List<DicValue> dicList1=执行sql语句   7条
         *    ....
         *    最终数据字典应该根据类型的不同保存为7个list对象
         *
         *
         *
         *
         */
        //取数据字典

        /**
         * 数据字典从业务层取出来，应该想业务层要7个list
         * 使用map集合，map集合中存储7个list
         *
         * map.put("applellation",dicList1);
         * map.put("clueState",dicList2);
         *
         */
        DicService dicService= (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map=dicService.getAllValue();
        //此时分好了类的7个list就保存在了map中，将map中的数据取出来
        Set<String> set=map.keySet();//取出map集合中所有的key

        //遍历key,取出value,放到application域对象中
        for(String key:set){
            application.setAttribute(key,map.get(key));
    }

        System.out.println("服务器缓存处理数据字典结束");


        //数据字典处理完毕之后，处理Stage2Prpperties文件
        //解析该文件，将这个文件中的键值对处理成java中能够解析的键值对

        /**
         *       处理Stage2Possibility.properties文件步骤：
         *                 解析该文件，将该属性文件中的键值对关系处理成为java中键值对关系（map）
         *
         *                 Map<String(阶段stage),String(可能性possibility)> pMap = ....
         *                 pMap.put("01资质审查",10);
         *                 pMap.put("02需求分析",25);
         *                 pMap.put("07...",...);
         *
         *                 pMap保存值之后，放在服务器缓存中
         *                 application.setAttribute("pMap",pMap);
         */

        Map<String,String> pmap=new HashMap<>();

        ResourceBundle resourceBundle=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e=resourceBundle.getKeys();//返回的是一个枚举类型，枚举：存储的是不可改变的值
        while (e.hasMoreElements()){
            String key=e.nextElement();
            //数据量大的时候，使用迭代器遍历，速度快
            String value=resourceBundle.getString(key);//通过key获得value

            //使用map存储数据
            pmap.put(key,value);
        }
                application.setAttribute("pmap",pmap);

    }
}
