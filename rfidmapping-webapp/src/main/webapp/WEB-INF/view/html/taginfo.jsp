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
		<h1>Information of ID ${tagId}</h1>
		<h2>Current location (x,y,z)</h2>
		<c:choose>
			<c:when test="${currentLocation!=null}"><p>(${currentLocation.getX()},${currentLocation.getY()},${currentLocation.getZ()})</p></c:when>
			<c:otherwise><p>Not available</p></c:otherwise>
		</c:choose>
		<h2>Read logs</h2>
		<c:choose>
			<c:when test="${readLogs!=null}">
				<table>
					<tr><th>Time of reading</th><th>Reader location (x,y,z,&alpha;,&delta;)</th><th>RSSI</th><th>Reading session ID</th></tr>
					<c:forEach items="${readLogs}" var="log">
						<tr>
							<td>${log.getReadDate()}</td>
							<td>(${log.getReaderLocation().getStartingPosition().getX()},${log.getReaderLocation().getStartingPosition().getY()},${log.getReaderLocation().getStartingPosition().getZ()},${log.getReaderLocation().getDirection().getAlpha()},${log.getReaderLocation().getDirection().getDelta()})</td>
							<td>${log.getRSSI()}</td>
							<td>${log.getReadingSession()}</td>
						</tr>
					</c:forEach>
				</table>
			</c:when>
			<c:otherwise><p>Not available</p></c:otherwise>
		</c:choose>
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
