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
		<h1>RFIDmapping</h1>
		<h2>Location-available IDs (${currentLocations.size()} IDs)</h2>
		<dl>
			<c:forEach items="${currentLocations}" var="locationEntry">
				<c:if test="${locationEntry.value!=null}">
					<c:url value="/html/taginfo" var="url">
						<c:param name="id" value="${locationEntry.key}" />
					</c:url>
					<dt><a href="${url}">${locationEntry.key}</a></dt>
					<dd>(${locationEntry.value.getX()},${locationEntry.value.getY()},${locationEntry.value.getZ()})</dd>
				</c:if>
			</c:forEach>
		</dl>
		<h2>Logged IDs (${loggedIDs.size()} IDs)</h2>
		<ul>
			<c:forEach items="${loggedIDs}" var="id">
				<c:url value="/html/taginfo" var="url">
					<c:param name="id" value="${id}" />
				</c:url>
				<li><a href="${url}">${id}</a></li>
			</c:forEach>
		</ul>
		<footer>
			<%= new java.util.Date() %>
		</footer>
	</body>
</html>
