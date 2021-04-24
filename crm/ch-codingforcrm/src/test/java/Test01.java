import com.crmdemo.crm.utils.DateTimeUtil;
import com.crmdemo.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test01 {
    public static void main(String[] args) {

        //设置登录失效时间  验证是否失效要和系统时间做比较
//        String expireTime="2021-10-10 10:10:10";
        //设置当前系统时间
//        Date date=new Date();
////        //System.out.println(date);//Fri Apr 16 16:31:59 CST 2021   进行日期格式化
////        SimpleDateFormat sdf=new SimpleDateFormat("yyy-mm-dd HH.mm.hh");
////        String str=sdf.format(date);
////        System.out.println(str);
////
        //调用工具类实现
//        String str= DateTimeUtil.getSysTime();
//        int count=expireTime.compareTo(str);
//        System.out.println(count);//返回的结果大于0就说明没有实现，结果小于0就说明已经失效了


//        设置锁定状态
//        String lockState="0";
//        if("0".equals(lockState)){
//            System.out.println("当前账号已锁定");
//        }
//


//        判断ip地址
//        String ip="196.168.3";
//////        String allowIp="196.168.1.1.234.6";
//////        if(allowIp.contains(ip)){
//////            System.out.println("有效的ip地址，允许访问系统");
//////        }else{
//////            System.out.println("ip地址受限，请联系管理员！");
//////        }


//        md5密码加密，传明文拿到密文
//        String pwd="134";
//        pwd=MD5Util.getMD5(pwd);
//        System.out.println(pwd);//202cb962ac59075b964b07152d234b70
    }
}
