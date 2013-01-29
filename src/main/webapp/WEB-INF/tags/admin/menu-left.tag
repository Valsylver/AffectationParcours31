<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="category" required="true" type="java.lang.String"%>
<%@ attribute name="stats-category" required="true"
	type="java.lang.String"%>
<%@ attribute name="synthese" required="false" type="java.lang.Boolean"%>
<%@ attribute name="abbreviation" required="false"
	type="java.lang.Boolean"%>
<%@ attribute name="job-sector" required="true"
	type="java.lang.ArrayList"%>
<%@ attribute name="job-sector" required="true"
	type="java.lang.ArrayList"%>
<%@ taglib tagdir="/WEB-INF/tags/admin" prefix="tags"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:choose>
	<c:when test="${category == 'stats'}">
		<ul class="nav nav-list">
			<li class="nav-header">Statistiques</li>
			<tags:stats-choose categoryToCompare="Synthese" category="${stats-category}"></tags:stats-choose>
			<li><a href="/admin/parcours/statistics">Parcours</a></li>
			<li><a href="/admin/filieres/statistics">Filières</a></li>
			<li><a href="/admin/statistics/eleves/all">Elèves</a></li>

			<li class="nav-header">Parcours</li>
			<li><a href="/admin/parcours/synthese/choix1">Synthèse</a></li>
			<c:forEach var="spec" items="${allPa}">
				<li><a
					href="/admin/parcours/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
			</c:forEach>

			<li class="nav-header">Filières</li>
			<li class="active"><a href="/admin/filieres/synthese/choix1">Synthèse</a></li>
			<c:forEach var="spec" items="${allFm}">
				<li><a
					href="/admin/filieres/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
			</c:forEach>
		</ul>
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>