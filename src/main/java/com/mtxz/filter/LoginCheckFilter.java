package com.mtxz.filter;

import com.alibaba.fastjson.JSON;
import com.mtxz.common.BaseContext;
import com.mtxz.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/15
 * Time: 10:05
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求uri
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);

        //需要放行url
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/user/loginout",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        //判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //放行
        if (check) {
            log.info("本次请求不用处理：{}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //判断后台用户登录状态
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录,id为：{}", request.getSession().getAttribute("employee"));

            //将id存入线程，在公共字段更新操作者时使用
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            //long id = Thread.currentThread().getId();
            //log.info("线程id为：{}", id);

            filterChain.doFilter(request, response);
            return;
        }

        //判断移动端用户登录状态
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录,id为：{}", request.getSession().getAttribute("user"));

            //将id存入线程，在公共字段更新操作者时使用
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            //long id = Thread.currentThread().getId();
            //log.info("线程id为：{}", id);

            filterChain.doFilter(request, response);
            return;
        }

        //未登录
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 检查本次请求是否放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
