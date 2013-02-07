<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<!-- Bootstrap -->
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
<link href="/css/student-admin-page.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li class="nav-header">Statistiques</li>
					<li><a href="/admin/statistics/synthese">Synthèse</a></li>
					<li><a href="/admin/parcours/statistics">Parcours</a></li>
					<li><a href="/admin/filieres/statistics">Filières</a></li>
					<li class="active"><a href="/admin/statistics/eleves/all">Elèves</a></li>

					<li class="nav-header">Parcours</li>
					<li><a href="/admin/parcours/synthese/choix1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}">
						<li><a
							href="/admin/parcours/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
					</c:forEach>

					<li class="nav-header">Filières</li>
					<li><a href="/admin/filieres/synthese/choix1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<li><a
							href="/admin/filieres/details/${spec.abbreviation}/choix1">${spec.abbreviation}</a></li>
					</c:forEach>
				</ul>
			</div>

			<div class="span7">
				<ul class="nav nav-pills">
					<li><a href="/admin/statistics/eleves/lol/pie-chart">Statistiques</a></li>
					<c:choose>
						<c:when test="${category == 'all'}">
							<li class="active"><a href="/admin/statistics/eleves/all">Dossiers
									complets (${nbreAll})</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/admin/statistics/eleves/all">Dossiers
									complets (${nbreAll})</a></li>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${category == 'partial'}">
							<li class="active"><a href="/admin/statistics/eleves/partial">Dossiers
									incomplets (${nbrePartial})</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/admin/statistics/eleves/partial">Dossiers
									incomplets (${nbrePartial})</a></li>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${category == 'no'}">
							<li class="active"><a href="/admin/statistics/eleves/no">Dossiers
									vides (${nbreNo})</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/admin/statistics/eleves/no">Dossiers vides (${nbreNo})</a></li>
						</c:otherwise>
					</c:choose>
				</ul>

				<c:if test="${category == 'all'}">
					<div class="alert alert-success">Elèves ayant déposé leurs
						documents et effectué tous leurs choix</div>
				</c:if>

				<c:if test="${category == 'partial'}">
					<div class="alert alert-block">Elèves n'ayant pas déposé
						tous leurs documents ou n'ayant pas effectué tous leurs choix</div>
				</c:if>

				<c:if test="${category == 'no'}">
					<div class="alert alert-error">Elèves n'ayant pas déposé tous
						leurs documents ni effectué tous leurs choix</div>
				</c:if>

				<c:forEach var="student" items="${results}">
									<a href="/admin/student/${student['login']}">${student['name']}</a>
									<br />
				</c:forEach>

			</div>
			<tags:rightColumnAdmin />
		</div>
	</div>


</body>
</html>
