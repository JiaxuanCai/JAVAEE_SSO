package com.cloud.sso.server.controller;

import com.cloud.sso.server.dao.UserDao;
import com.cloud.sso.server.model.TicketGrangtingTicket;
import com.cloud.sso.server.model.User;
import com.cloud.sso.server.storage.JVMCache;
import com.cloud.sso.server.storage.UrlMap;
import com.cloud.sso.server.util.RedirectUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String clientToken = (String) session.getAttribute("CAS-ST");
        System.out.println("server: " + clientToken);
        String logoutUrl = request.getParameter("service");
        // 主动退出
        // 向认证中心发送注销请求
        System.out.println("CAS logout start");
        System.out.println(request.getRequestURL());
        String logoutInterfaceUrl = "/logout";
        String TGTId = "";
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
                if ("CAS-TGC".equals(cookie.getName())) {
                    TGTId = cookie.getValue();
                    break;
                }
            }
        }
        System.out.println("server get CAS-TGC: " + TGTId);
        TicketGrangtingTicket TGT = JVMCache.TGT_CACHE.get(TGTId);
        for(Map.Entry<String, String> entry : TGT.serviceMap.entrySet()){
            String ST = entry.getKey();
            String service = entry.getValue();
            System.out.println("ST= " + ST);
            // 通知service logout
            PostMethod postMethod = new PostMethod(service + logoutInterfaceUrl);
            System.out.println("post to: " + service + logoutInterfaceUrl);
            postMethod.addParameter("CAS-ST", ST);
            HttpClient httpClient = new HttpClient();
            try {
                httpClient.executeMethod(postMethod);
                postMethod.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 注销后重定向
//        response.sendRedirect(logoutUrl);

        // 注销本地会话
        TicketGrangtingTicket removedTGT = JVMCache.TGT_CACHE.remove(TGTId);
        if (removedTGT != null) {
            session.removeAttribute("CAS-TGC");
        }

    }
}
