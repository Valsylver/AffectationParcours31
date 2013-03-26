<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale Marseille</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.min.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<li class="nav-header">Statistiques</li>
					<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/choice1">Choix</a></li>
					<li class="active"><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/synthese">Dossiers</a></li>
					<li class="dropdown-submenu"><a href="#">Répartition parcours</a>
						<ul class="dropdown-menu">
							<c:forEach var="js" items="${allJs}">
								<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/inverse-repartition/js/${js.abbreviation}">${js.abbreviation}</a></li>
							</c:forEach>
						</ul></li>
					<li class="dropdown-submenu"><a href="#">Répartition filières</a>
						<ul class="dropdown-menu">
							<c:forEach var="ic" items="${allIc}">
								<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/inverse-repartition/ic/${ic.abbreviation}">${ic.abbreviation}</a></li>
							</c:forEach>
						</ul></li>
					<li class="dropdown-submenu"><a href="#">Autres choix</a>
						<ul class="dropdown-menu">
							<li class="dropdown-submenu"><a href="#">Parcours</a>
								<ul class="dropdown-menu">
									<c:forEach var="ic" items="${allIc}">
										<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/repartition-other-choice2/1/${ic.abbreviation}">${ic.abbreviation}</a></li>
									</c:forEach>
								</ul></li>
							<li class="dropdown-submenu"><a href="#">Filières</a>
								<ul class="dropdown-menu">
									<c:forEach var="js" items="${allJs}">
										<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/repartition-other-choice2/2/${js.abbreviation}">${js.abbreviation}</a></li>
									</c:forEach>
								</ul></li>
						</ul></li>

					<li class="nav-header">Parcours</li>
					<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/improvement-course/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}">
						<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/improvement-course/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>

					<li class="nav-header">Filières</li>
					<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/job-sector/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/job-sector/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>
				</ul>
			</div>

			<div class="span7">
				<ul class="nav nav-pills">
					<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/synthese">Statistiques</a></li>
					<c:choose>
						<c:when test="${category == 'all'}">
							<li class="active"><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/details/all">Dossiers complets (${nbreAll})</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/details/all">Dossiers complets (${nbreAll})</a></li>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${category == 'partial'}">
							<li class="active"><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/details/partial">Dossiers incomplets (${nbrePartial})</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/details/partial">Dossiers incomplets (${nbrePartial})</a></li>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${category == 'no'}">
							<li class="active"><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/details/no">Dossiers vides (${nbreNo})</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/details/no">Dossiers vides (${nbreNo})</a></li>
						</c:otherwise>
					</c:choose>
				</ul>

				<c:if test="${category == 'all'}">
					<div class="alert alert-success">Elèves ayant déposé leurs documents et effectué tous leurs choix.</div>
				</c:if>

				<c:if test="${category == 'partial'}">
					<div class="alert alert-block">Elèves n'ayant pas déposé tous leurs documents ou n'ayant pas effectué tous leurs choix.</div>
				</c:if>

				<c:if test="${category == 'no'}">
					<div class="alert alert-error">Elèves n'ayant pas déposé tous leurs documents ni effectué tous leurs choix.</div>
				</c:if>

				<c:forEach var="student" items="${results}">
					<a href="${pageContext.request.contextPath}/admin/run/main/student/${student.login}">${student.name}</a>
					<br />
				</c:forEach>

			</div>

			<div class="span3">
				<tags:rightColumnAdmin />
			</div>
		</div>
	</div>
</body>
</html>
