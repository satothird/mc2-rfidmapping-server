<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<meta charset="utf-8">
		<title>RFIDmapping</title>
	</head> 
	<body>
		<p>${msg}</p>
		<footer>
			<ul>
				<li>
					<c:url value="/html" var="url" />
					<a href="${url}">Back to top</a>
				</li>
			</ul>
			<%= new java.util.Date() %>
		</footer>
	</body>
</html>
