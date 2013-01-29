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
					<tags:leftColumnAdmin />

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
				<h3>
					<legend>${student.details.fullName}</legend>
				</h3>
				<h4>Voeux</h4>
				<table class="table table-bordered table-striped table-condensed">
					<thead>
						<tr>
							<th></th>
							<th>Parcours</th>
							<th>Filières</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><b>Choix 1</b>
							<td>${student.improvementCourseChoice.choice1}</td>
							<td>${student.jobSectorChoice.choice1}</td>
						</tr>
						<tr>
							<td><b>Choix 2</b>
							<td>${student.improvementCourseChoice.choice2}</td>
							<td>${student.jobSectorChoice.choice2}</td>
						</tr>
						<tr>
							<td><b>Choix 3</b>
							<td>${student.improvementCourseChoice.choice3}</td>
							<td>${student.jobSectorChoice.choice3}</td>
						</tr>
						<tr>
							<td><b>Choix 4</b>
							<td>${student.improvementCourseChoice.choice4}</td>
							<td>${student.jobSectorChoice.choice4}</td>
						</tr>
						<tr>
							<td><b>Choix 5</b>
							<td>${student.improvementCourseChoice.choice5}</td>
							<td>${student.jobSectorChoice.choice5}</td>
						</tr>
					</tbody>
				</table>

				<h4>Documents</h4>
				<c:choose>
					<c:when test="${hasFilledResume}">
						<a href="/resources/cv/cv_${student.details.login}.pdf">CV</a>
					</c:when>
					<c:otherwise>
						Cet élève n'a pas encore déposé son CV.
					</c:otherwise>
				</c:choose>
				<br />

				<c:choose>
					<c:when test="${hasFilledLetterIc}">
						<a
							href="/resources/lettres/parcours/lettre_parcours_${student.details.login}.pdf">Lettre
							parcours</a>
					</c:when>
					<c:otherwise>
						Cet élève n'a pas encore déposé sa lettre de motivation pour son choix de parcours d'approfondissement.
					</c:otherwise>
				</c:choose>
				<br />

				<c:choose>
					<c:when test="${hasFilledLetterJs}">
						<a
							href="/resources/lettres/filieres/lettre_filiere_${student.details.login}.pdf">Lettre
							filière</a>
					</c:when>
					<c:otherwise>
						Cet élève n'a pas encore déposé sa lettre de motivation pour son choix de filière métier.
					</c:otherwise>
				</c:choose>
				<br /> <br />

				<h4>Résultats</h4>
				<div class="marge">
					<h5>Contentieux</h5>
				</div>
				<c:choose>
					<c:when test="${fn:length(student.results.contentious) > 0 }">
						<table class="table table-bordered table-striped table-condensed">
							<thead>
								<tr>
									<th>Cycle</th>
									<th>Semestre</th>
									<th>UE</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="contentious"
									items="${student.results.contentious}">
									<tr>
										<td>${contentious.cycle}</td>
										<td>${contentious.semester}</td>
										<td>${contentious.ueCode}</td>
									</tr>
								</c:forEach>
						</table>
					</c:when>
					<c:otherwise>
						Cet élève n'a aucun contentieux.
					</c:otherwise>
				</c:choose>
				<div class="marge">
					<h5>Moyennes GPA</h5>
				</div>
				<table class="table-bordered table-striped table-condensed">
					<thead>
						<tr>
							<th>Semestre</th>
							<th>Moyenne Gpa</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>5</td>
							<td>${student.results.gpaMeanS5}</td>
						</tr>
						<tr>
							<td>6</td>
							<td>${student.results.gpaMeanS6}</td>
						</tr>
						<tr>
							<td>7</td>
							<td>${student.results.gpaMeanS7}</td>
						</tr>
					</tbody>
				</table>
				
				<br />
				<br />
			</div>

			<tags:rightColumnAdmin />
		</div>
	</div>

</body>
</html>
