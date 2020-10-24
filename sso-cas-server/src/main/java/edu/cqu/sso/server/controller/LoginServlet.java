package edu.cqu.sso.server.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.cqu.sso.server.dao.UserDao;
import edu.cqu.sso.server.model.TicketGrangtingTicket;
import edu.cqu.sso.server.model.User;
import edu.cqu.sso.server.storage.JVMCache;
import edu.cqu.sso.server.util.RedirectUtil;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String service = request.getParameter("service");
        // 登录请求
        // 验证用户
        UserDao userDao = new UserDao();
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            response.sendRedirect("/index.jsp?service=" + service);
            return;
        }
        // 验证成功
        // 生成TGC
        String uuid = UUID.randomUUID().toString();
        String TGCValue = "TGC-" + uuid;
        Cookie cookie = new Cookie("CAS-TGC", TGCValue);
        cookie.setPath("/");
        System.out.println("server set cookie CAS-TGC: " + TGCValue);
        response.addCookie(cookie);

        // 缓存TGT
        // TGT封装了Cookie值以及此Cookie值对应的用户信息
        TicketGrangtingTicket TGT = new TicketGrangtingTicket(username);
        JVMCache.TGT_CACHE.put(TGCValue, TGT);

        // 使用TGT签发ST，并缓存ST
        String STValue = "ST-" + UUID.randomUUID().toString();
        JVMCache.ST_CACHE.put(STValue, username);
        // 注册成功，在浏览器和cas服务器见建立信任，缓存url用于退出
        TGT.serviceMap.put(STValue, service);
        System.out.println("server store TGT, id: " + TGT.serviceMap.get(STValue));
        // 判断service跳转情况
        if (null != service) {
            String url = RedirectUtil.getRedirectUrlWithTicket(service, STValue);
            response.sendRedirect(url);
        } else {
            response.sendRedirect("/index.jsp");
        }

    }

}