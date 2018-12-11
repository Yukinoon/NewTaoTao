<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<%
	String callback=request.getParameter("callback");
	if(callback==null){
		out.print("{\"abc\":123}");
	}else{
		out.print(callback+"({\"abc\":123})");
	}
	
%>