<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<% 
String quantity = request.getParameter("quantity");
String no = request.getParameter("no");


%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%=no %>

<%=quantity %>
</body>
</html>