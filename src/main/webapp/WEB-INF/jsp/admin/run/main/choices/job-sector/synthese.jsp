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
					<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/form/synthese">Dossiers</a></li>
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
									<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/repartition-other-choice2/1/${ic.abbreviation}">${ic.abbreviation}</a>
									</li>
								</c:forEach>
								</ul>
							</li>
							<li class="dropdown-submenu"><a href="#">Filières</a>
								<ul class="dropdown-menu">
								<c:forEach var="js" items="${allJs}">
									<li><a href="${pageContext.request.contextPath}/admin/run/main/statistics/repartition-other-choice2/2/${js.abbreviation}">${js.abbreviation}</a>
									</li>
								</c:forEach>
								</ul>
							</li>
						</ul></li>

					<li class="nav-header">Parcours</li>
					<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/improvement-course/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}">
						<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/improvement-course/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>

					<li class="nav-header">Filières</li>
					<li class="active"><a href="${pageContext.request.contextPath}/admin/run/main/choices/job-sector/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/job-sector/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>
				</ul>
			</div>
			<div class="span7">
				<ul class="nav nav-pills">
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:choose>
							<c:when test="${i == order}">
								<li class="active"><a href="${pageContext.request.contextPath}/admin/run/main/choices/job-sector/synthese/choice${order}">Choix ${i}</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.request.contextPath}/admin/run/main/choices/job-sector/synthese/choice${i}">Choix ${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>

				<c:forEach var="specialization" items="${allJs}" varStatus="status">
					<span class="lead"> <a href="${pageContext.request.contextPath}/admin/common/edit/js/${specialization.abbreviation}"> ${specialization.name} (${specialization.abbreviation}) </a>
					</span>
					<br />
					<br />
					<c:choose>
						<c:when test="${fn:length(allStudents[status.index]) > 0}">
							<table class="table table-bordered table-striped">
								<c:choose>
									<c:when test="${running}">
										<tbody>
											<c:forEach var="student" items="${allStudents[status.index]}">
												<tr>
													<td><a href="${pageContext.request.contextPath}/admin/run/main/student/${student.login}">${student.name}</a></td>
												</tr>
											</c:forEach>
										</tbody>
									</c:when>
									<c:otherwise>
										<tbody>
											<c:forEach var="student" items="${allStudents[status.index]}">
												<c:choose>
													<c:when test="${student.validated}">
														<tr class="success">
															<td><a href="${pageContext.request.contextPath}/admin/run/main/student/${student.login}">${student.name}</a></td>
														</tr>
													</c:when>
													<c:otherwise>
														<tr class="error">
															<td><a href="${pageContext.request.contextPath}/admin/run/main/student/${student.login}">${student.name}</a></td>
														</tr>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</tbody>
									</c:otherwise>
								</c:choose>
							</table>
						</c:when>
						<c:otherwise>
							<p>
								Aucun elève n'a choisi cette filière métier en
								<c:choose>
									<c:when test="${order == 1}">
										1er choix.
									</c:when>
									<c:otherwise>
										${order}ème choix.
									</c:otherwise>
								</c:choose>
							</p>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
			<div class="span3">
				<tags:rightColumnAdmin />
			</div>
		</div>
	</div>
</body>
</html>
