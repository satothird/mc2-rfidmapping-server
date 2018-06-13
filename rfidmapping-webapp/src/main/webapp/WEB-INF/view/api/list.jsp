<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="first" value="true" />
[
<c:forEach items="${currentLocations}" var="locationEntry">
	<c:if test="${locationEntry.value!=null}">
		<c:if test="${!first}">
			,
		</c:if>
		<c:set var="first" value="false" />
		{
			"tagId":"${locationEntry.key}",
			"position":{
				"x":${locationEntry.value.getX()},
				"y":${locationEntry.value.getY()},
				"z":${locationEntry.value.getZ()}
			}
		}
	</c:if>
</c:forEach>
]