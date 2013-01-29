<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/admin/links" prefix="link"%>
<%@ attribute name="category" required="true"
	type="java.lang.String"%>
<%@ attribute name="categoryToCompare" required="true"
	type="java.lang.String"%>

<c:choose>
	<c:when test="${category == categoryToCompare}">
		<li class="active"><link:stats-synthese text=${synthese}></link:stats-synthese></li>
	</c:when>
	<c:otherwise>
		<li><link:stats-synthese text=${synthese}></link:stats-synthese></li>
	</c:otherwise>
</c:choose>