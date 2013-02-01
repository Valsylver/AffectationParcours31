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
					<li class="active"><a href="/admin/parcours/synthese/choix1">Synthèse</a></li>
					<c:forEach var="spec" items="${allIc}" varStatus="status">
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
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:choose>
							<c:when test="${i == order}">
								<li class="active"><a
									href="/admin/parcours/synthese/choix${i}">Choix ${i}</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="/admin/parcours/synthese/choix${i}">Choix
										${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
				<hr>

				<c:forEach var="specialization" items="${allIc}" varStatus="status">
					<span class="lead"> <a
						href="/admin/config/modif/parcours/${specialization.abbreviation}">
							${specialization.name} (${specialization.abbreviation}) </a>
					</span>
					<br />
					<br />
					<c:choose>
						<c:when test="${fn:length(allStudents[status.index]) > 0}">
							<table class="table table-bordered table-striped">
								<c:choose>
									<c:when test="${running}">
										<thead>
											<tr>
												<th>Nom</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="student" items="${allStudents[status.index]}">
												<tr>
													<td><a href="/admin/student/${student.login}">${student.name}</a></td>
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
											<c:forEach var="student" items="${allStudents[status.index]}">
												<c:choose>
													<c:when test="${student.validated}">
														<tr class="success">
															<td><a href="/admin/student/${student.login}">${student.name}</a>
															</td>
														</tr>
													</c:when>
													<c:otherwise>
														<tr class="error">
															<td><a href="/admin/student/${student.login}">${student.name}</a>
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
				</c:forEach>
			</div>
			<tags:rightColumnAdmin />
		</div>
	</div>
</body>
</html>
