<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<title>Affectation parcours/filière 3ème année Centrale
	Marseille</title>
<!-- Bootstrap -->
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">
</head>
<body>
	<div class="container">

		<tags:headerResponsible
			title="${specialization.name} (${specialization.abbreviation})" />

		<div class="row">
			<div class="span2">
				<ul class="nav nav-list">

					<li class="nav-header">${specialization.abbreviation}</li>
					<c:forEach var="i" begin="1" end="5" step="1">
						<c:choose>
							<c:when test="${i == order}">
								<li class="active"><a href="/responsable/${i}">Choix
										${i}</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="/responsable/${i}">Choix ${i}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<li class="nav-header">Synthese</li>
					<li><a href="/responsable/statistics">Statistiques</a></li>
					<li><a>Autres parcours</a></li>
				</ul>
			</div>

			<div class="span7">
				<c:choose>
					<c:when test="${fn:length(allStudents) > 0}">
						<form:form action="/responsable/edit-validation" method="POST"
							commandName="studentsValidation" enctype="multipart/form-data">
							<button class="btn btn-primary" name="commit" type="submit">
								<i class="icon-white icon-ok"></i> Sauvegarder les modifications
							</button>
							<br />
							<br />
							<c:if test="${not empty successMessage}">
								<div class="alert alert-success">${successMessage}</div>
							</c:if>
							<br />
							<c:forEach var="student" items="${allStudents}"
								varStatus="status">
								<form:input path="students[${status.index}]"
									style="display:none" value="${student.login}"></form:input>
							</c:forEach>
							<table id="student"
								class="table table-bordered table-striped tablesorter">
								<thead>
									<tr>
										<th>Nom</th>
										<th>Validation</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="student" items="${allStudents}"
										varStatus="status">
										<tr>
											<td><a href="/responsable/student/${student.login}">${student.name}</a></td>
											<td><form:checkbox path="validated[${status.index}]"></form:checkbox></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</form:form>
					</c:when>
					<c:otherwise>
						<p>
							<c:choose>
								<c:when test="${specialization.type == 'ic'}">
									Aucun elève n'a pour l'instant choisi ce parcours d'approfondissement en 
								</c:when>
								<c:otherwise>
									Aucun elève n'a pour l'instant choisi cette filière métier en 
								</c:otherwise>
							</c:choose>
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
		</div>
	</div>
</body>
</html>