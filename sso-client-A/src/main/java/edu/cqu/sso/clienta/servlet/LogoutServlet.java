package edu.cqu.sso.clienta.servlet;

import edu.cqu.sso.clienta.storage.Cache;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("----------client start logout--------------");
        // 处理退出请求 本来应该用报文封装
        String ST = request.getParameter("CAS-ST");
        String url = URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
        System.out.println("clientA get ST: " + ST);
        if (ST == null || "".equals(ST)){ // 说明是主动退出
            System.out.println("client A request to logout");
            response.sendRedirect("http://localhost:8088/logout?service=" + url);
//            PostMethod postMethod = new PostMethod("http://localhost:8088/logout?service=" + url);
//            HttpClient httpClient = new HttpClient();
//            try {
//                httpClient.executeMethod(postMethod);
//                postMethod.releaseConnection();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }else {
            System.out.println("client A reponse to logout");
            // 接收来自服务器的通知 映射关系
            // 仅供测试
            HttpSession session = Cache.sessionMap.get(ST);
            if (session != null){
                System.out.println("logout: sessionId=" + session.getId());
                session.removeAttribute("isLogin");
                session.invalidate();
                Cache.map.remove("CAS-ST");
            }
            System.out.println("finish logout");
        }

    }
}
