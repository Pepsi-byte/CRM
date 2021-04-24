package com.crmdemo.crm.setting.web.Filter;

import com.crmdemo.crm.setting.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class loginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("进入到过滤是否登录的过滤器！");

        /**
         * 验证是否进行了登录，由于永固的信息被存放在了session域中，
         * 所以，如果session域中存在用户的的信息，就说明用户登录过了
         */
        //servletRequest是HttpServletRequest的父类，他没有获取session的方法，所以需要强制类型转换
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        //登录页面需要放行，不放行的话就会一直死循环
        //这是不应该拦截的资源，必须放行的请求
        String path=request.getServletPath();
        if("/login.jsp".equals(path)||"/setting/user/login.do".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);

            //应该拦截的资源
        }else{
            //现在就可以使用request来获取session
            HttpSession session=request.getSession();
            //通过session来拿到session中的数据User
            User user= (User) session.getAttribute("user");
            //设置session的最大生存时间，以秒为单位，到了这个时间sesson就会自动失效
            session.setMaxInactiveInterval(30*60);

            //如果user不为空，就说明用户登录过，user是用户输入账户和密码之后，登录成功的话，后台穿过来的用户的信息
            if(user!=null){

                //用户登录过，放行
                filterChain.doFilter(servletRequest,servletResponse);
            }else{
                //用户还没有登录，要重定向到登录的页面，如果使用转发的话，地址栏不会改变
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }

        }


    }

    @Override
    public void destroy() {

    }
}
