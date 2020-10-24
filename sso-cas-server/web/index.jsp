<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
    String service = request.getParameter("service");
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    <style type="text/css">
        /*表单样式*/
        .myformdiv {
            background-color: #56a66e;
            opacity: 0.85;
        }
        .col-center-block {
            position: absolute;
            top: 50%;
            -webkit-transform: translateY(-50%);
            -moz-transform: translateY(-50%);
            -ms-transform: translateY(-50%);
            -o-transform: translateY(-50%);
            transform: translateY(-50%);
        }

    </style>
</head>
<body>
<%--<form action="/user/login" method="post">--%>
<%--    userName: <input type="text" name="username"/> <br>--%>
<%--    password: <input type="text" name="password"/> <br>--%>
<%--    <input type="hidden" name="service" value="<%=service%>"/> <br>--%>
<%--    <input type="submit" value="submit"/>--%>
<%--</form>--%>

     <!-- 登录表单 -->
<div class="row justify-content-center ">

    <!-- 下面是我要居中的div，添加.col-center-block -->
    <div class="col-center-block col-sm-4 col-xs-4 jumbotron myformdiv ">
        <h2 class="text-center">Welcome To Login!</h2>
        <br>
    <form action="/user/login" method="post" class ="text-center">
         <div class="form-group">
             <label for="user" style="display:inline;">username </label>
             <input type="text" id="user" class="form-control" name="username" style="display:inline;width:200px;"autocomplete="off" />
         </div>
         <div class="form-group">
             <label for="password" style="display:inline;">password </label>
             <input type="text" id="password" class="form-control" name="password" style="display:inline;width:200px;"autocomplete="off" />
         </div>
         <input type="hidden" name="service" value="<%=service%>"/>
         <button type="submit" class="btn btn-primary" style="background: darkgreen">登录</button>
     </form>
        </div>
</div>
</body>
</html>