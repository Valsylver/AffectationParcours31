<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<tags:headerAdmin run="<%=true%>" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">
					<tags:leftColumnAdmin />

					<li class="nav-header">Parcours</li>
					<li><a href="/admin/run/main/choices/improvement-course/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}">
						<c:choose>
							<c:when test="${spec.abbreviation == abbreviation}">
								<li class="active"><a
									href="/admin/run/main/choices/improvement-course/details/${abbreviation}/choice1">${spec.abbreviation}</a></li>
							</c:when>
							<c:otherwise>
								<li><a
									href="/admin/run/main/choices/improvement-course/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<li class="nav-header">Filières</li>
					<li><a href="/admin/run/main/choices/job-sector/synthese/choice1">Synthèse</a></li>
					<c:forEach var="spec" items="${allJs}" varStatus="status">
						<li><a
							href="/admin/run/main/choices/job-sector/details/${spec.abbreviation}/choice1">${spec.abbreviation}</a></li>
					</c:forEach>
				</ul>
			</div>
			<div class="span8">
				<ul class="nav nav-pills">
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:choose>
							<c:when test="${i == order}">
								<li class="active"><a
									href="/admin/run/main/choices/improvement-course/details/${abbreviation}/choice${i}">Choix
										${i}</a></li>
							</c:when>
							<c:otherwise>
								<li><a
									href="/admin/run/main/choices/improvement-course/details/${abbreviation}/choice${i}">Choix
										${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
				<hr>

				<span class="lead"> <a
					href="/admin/config/modif/parcours/${abbreviation}">
						${specialization.name} (${specialization.abbreviation}) </a>
				</span> <br /> <br />
				<c:choose>
					<c:when test="${fn:length(allStudents) > 0}">
						<table class="table table-bordered table-striped">
							<c:choose>
								<c:when test="${running}">
									<thead>
										<tr>
											<th>Nom</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="student" items="${allStudents}">
											<tr>
												<td><a href="/admin/run/main/student/${student.login}">${student.name}</a></td>
											</tr>
										</c:forEach>
									</tbody>
								</c:when>
								<c:otherwise>
									<thead>
										<tr>
											<th>Nom</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="student" items="${allStudents}">
											<c:choose>
												<c:when test="${student.validated}">
													<tr class="success">
														<td>
															<a href="/admin/run/main/student/${student.login}">${student.name}</a>
														</td>
													</tr>
												</c:when>
												<c:otherwise>
													<tr class="error">
														<td>
															<a href="/admin/run/main/student/${student.login}">${student.name}</a>
														</td>
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
							Aucun elève n'a pour l'instant choisi ce parcours
							d'approfondissement en
							<c:choose>
								<c:when test="${order == 1}">
									1er choix
								</c:when>
								<c:otherwise>
									${order}ème choix.
								</c:otherwise>
							</c:choose>
						</p>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="span2">
				<tags:rightColumnAdmin />
			</div>
		</div>
	</div>
</body>
</html>