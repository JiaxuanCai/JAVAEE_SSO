package com.cloud.sso.pro.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cloud.sso.pro.storage.Cache;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class SSOClientFilter implements Filter {

    public void destroy() {
    }
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        System.out.println(request.getRequestURI());
        if (request.getRequestURI().contains("logout")) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("client: sessionId=" + session.getId());
        String username = (String) session.getAttribute("username");
        System.out.println("session username: " + username);
        String ticket = request.getParameter("ticket");
        String url = URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
        System.out.println(session.getAttribute("isLogin"));
        if (null == session.getAttribute("isLogin")) {
            if (null != ticket && !"".equals(ticket)) {
                PostMethod postMethod = new PostMethod("http://localhost:8088/ticket");
                postMethod.addParameter("ticket", ticket);
                HttpClient httpClient = new HttpClient();
                try {
                    httpClient.executeMethod(postMethod);
                    username = postMethod.getResponseBodyAsString();
                    postMethod.releaseConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (null != username && !"".equals(username)) {
                    session.setAttribute("CAS-ST", ticket);
                    session.setAttribute("isLogin", true);
                    session.setAttribute("username", username);
//                    Cache.map.put("CAS-ST", ticket);
                    Cache.sessionMap.put(ticket, session);
                    System.out.println("put into map: key CAS-ST, value " + ticket);
                    response.sendRedirect("list");
                } else {
                    response.sendRedirect("http://localhost:8088/index.jsp?service=" + url);
                }
            } else {
                response.sendRedirect("http://localhost:8088/index.jsp?service=" + url);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
    public void init(FilterConfig arg0) throws ServletException {
    }

}