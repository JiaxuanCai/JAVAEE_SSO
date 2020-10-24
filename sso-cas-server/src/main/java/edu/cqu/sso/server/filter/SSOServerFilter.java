package edu.cqu.sso.server.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.cqu.sso.server.model.TicketGrangtingTicket;
import edu.cqu.sso.server.storage.JVMCache;
import edu.cqu.sso.server.util.RedirectUtil;

public class SSOServerFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        System.out.println("sso filter: " + request.getRequestURI());
        if (request.getRequestURI().contains("logout")){
            System.out.println("cas logout");
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().contains("user/login")){
            System.out.println("cas login");
            filterChain.doFilter(request, response);
            return;
        }
        String service = request.getParameter("service");
        String ticket = request.getParameter("ticket");
        Cookie[] cookies = request.getCookies();
        // 得到request中的TGC
        String TGCValue = "";
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if ("CAS-TGC".equals(cookie.getName())) {
                    TGCValue = cookie.getValue();
                    break;
                }
            }
        }

        // 以此Cookie值为key查询缓存中是否有TGT
        if(JVMCache.TGT_CACHE.containsKey(TGCValue)){
            // 如果有，表示用户已经在CAS端登录，则拿到TGT
            TicketGrangtingTicket TGT = JVMCache.TGT_CACHE.get(TGCValue);

            // 如果不需要跳转，则这里直接方形
            if (null == service){
                filterChain.doFilter(servletRequest, servletResponse);
            }else{
                // 如果需要跳转，则使用TGT签发ST，并缓存ST
                String STValue = "ST-" + UUID.randomUUID().toString();
                JVMCache.ST_CACHE.put(STValue, STValue);
                // 注册成功，在浏览器和cas服务器见建立信任，缓存url用于退出
                TGT.serviceMap.put(STValue, service);
                System.out.println("ServiceMap " + TGT.serviceMap.get(STValue));
                // 如果需要跳转，在url上附加ST ticket
                String url = RedirectUtil.getRedirectUrlWithTicket(service, STValue);
                response.sendRedirect(url);
            }
        } else {
            // 如果没有查到TGT，表示未登录，直接返回
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void init(FilterConfig arg0) throws ServletException {
    }

}